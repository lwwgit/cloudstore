package com.example.cloudstore.service.impl;


import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.service.CopyFileService;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CopyFileServiceImpl implements CopyFileService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GlobalFunction globalFunction;


    /**
     * 判断文件（夹）是否存在
     * 不存在 false
     * 存在  true
     * @param filePath
     * @return
     * @throws IOException
     */
    public boolean checkIsExist(String filePath) throws IOException {
        FileSystem fs = globalFunction.getHadoopFileSystem();
        Path path = new Path(filePath);

        boolean isExists = false;
        try {
            isExists = fs.exists(path);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        if (!isExists) {
            return false;//文件（夹）不存在
        }  else {
            return  true;//文件（夹）存在
        }
    }


    /**
     * hdfs之间文件的复制
     * 使用FSDataInputStream来打开文件open(Path p)
     * 使用FSDataOutputStream开创建写到的路径create(Path p)"/huabin/hadoop.tar.gz"
     * 使用 IOUtils.copyBytes(FSDataInputStream,FSDataOutputStream,int buffer,Boolean isClose)来进行具体的读写
     * 说明：
     *  1.java中使用缓冲区来加速读取文件，这里也使用了缓冲区，但是只要指定缓冲区大小即可，不必单独设置一个新的数组来接受
     *  2.最后一个布尔值表示是否使用完后关闭读写流。通常是false，如果不手动关会报错的

     */
    public boolean copyFile(String srcPath,String dstPath) throws IOException {

        FileSystem fs = globalFunction.getHadoopFileSystem();

        String filename = srcPath.substring(srcPath.lastIndexOf("/") + 1);
        String checkName = dstPath + "/" + filename;
        boolean checkResult = checkIsExist(checkName);
        if (checkResult == false) {
            Path inPath = new Path(srcPath);
            Path outPath = new Path(checkName);


            FSDataInputStream hdfsIn = null;
            FSDataOutputStream hdfsOut = null;
            try {
                hdfsIn = fs.open(inPath);
                hdfsOut = fs.create(outPath);
                IOUtils.copyBytes(hdfsIn, hdfsOut, 1024 * 1024 * 64, true);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
//            } finally {
//                try {
//                    hdfsOut.close();
//                    hdfsIn.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 复制一个目录下面的所有文件
     * @param src   需要复制的文件夹或文件
     * @param dsc   目的地
     * @throws Exception
     */
    public boolean copyDir(String src,String dsc) throws IOException {
        FileSystem fs = globalFunction.getHadoopFileSystem();
        Path srcPath = new Path(src);
        String[] strs = src.split("/");
        String lastName = strs[strs.length - 1];
        String checkPath = dsc + "/" + lastName;
        if (fs.isDirectory(srcPath)) {    //复制的是文件夹
            boolean checkResult = checkIsExist(checkPath);//判断文件夹是否在目录下存在
            if (checkResult == false) {
                fs.mkdirs(new Path(dsc + "/" + lastName));
                //遍历
                FileStatus[] fileStatus = fs.listStatus(srcPath);
                for (FileStatus fileSta : fileStatus) {
                    copyDir(fileSta.getPath().toString(), dsc + "/" + lastName);
                }
                return true;
            }
            return false;


        }else {
            fs.mkdirs(new Path(dsc));
            System.out.println("src"+src+"\n"+dsc);
            if (copyFile(src, dsc) == true) {
                return true;
            } else {
                return false;
            }
        }
    }
}

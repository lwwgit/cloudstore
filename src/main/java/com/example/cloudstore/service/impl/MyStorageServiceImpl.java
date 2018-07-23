package com.example.cloudstore.service.impl;


import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.Constants;
import com.example.cloudstore.domain.Md5;
import com.example.cloudstore.domain.MultipartFileParam;
import com.example.cloudstore.repository.Md5Repository;
import com.example.cloudstore.service.Md5service;
import com.example.cloudstore.service.MyStorageService;
import com.example.cloudstore.utils.FileMD5Util;

import com.example.cloudstore.utils.GetUserInfoUtil;
import net.bytebuddy.asm.Advice;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Author jitdc
 * @Date Create in 17:28 2018/7/5
 * @Description:
 */
@Service
public class MyStorageServiceImpl implements MyStorageService {

    private final Logger logger = LoggerFactory.getLogger(MyStorageServiceImpl.class);
    // 保存文件的根目录
    private Path rootPaht;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Md5Repository md5Repository;
    @Autowired
    private Md5service md5service;

    //这个必须与前端设定的值一致
    @Value("${breakpoint.upload.chunkSize}")
    private long CHUNK_SIZE;

    @Value("${breakpoint.upload.dir}")
    private String finalDirPath;

    @Value("${breakpoint.upload.tmp}")
    private String tmpDirPath;
    @Autowired
    private UploadToHdfsService uploadToHdfsService;

    @Autowired
    public MyStorageServiceImpl(@Value("${breakpoint.upload.dir}") String location) {
        this.rootPaht = Paths.get(location);
    }

    @Override
    public void deleteAll() {
        logger.info("开发初始化清理数据，start");
       // FileSystemUtils.deleteRecursively(rootPaht.toFile());
        stringRedisTemplate.delete(Constants.FILE_UPLOAD_STATUS);
        stringRedisTemplate.delete(Constants.FILE_MD5_KEY);
        logger.info("开发初始化清理数据，end");
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootPaht);
        } catch (FileAlreadyExistsException e) {
            logger.error("文件夹已经存在了，不用再创建。");
        } catch (IOException e) {
            logger.error("初始化root文件夹失败。", e);
        }
    }

    @Override
    public void uploadFileRandomAccessFile(MultipartFileParam param) throws IOException {
        String fileName = param.getName();
        String tempDirPath = finalDirPath + param.getMd5();
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(tempDirPath);
        File tmpFile = new File(tempDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
        long offset = CHUNK_SIZE * param.getChunk();
        //定位到该分片的偏移量
        accessTmpFile.seek(offset);
        //写入该分片数据
        accessTmpFile.write(param.getFile().getBytes());
        // 释放
        accessTmpFile.close();

        boolean isOk = checkAndSetUploadProgress(param, tempDirPath);
        if (isOk) {
            boolean flag = renameFile(tmpFile, fileName);
            System.out.println("upload complete !!" + flag + " name=" + fileName);
        }
    }

    @Override
    public void uploadFileByMappedByteBuffer(MultipartFileParam param, String username,String path) throws Exception {
        String fileName = param.getName();
        String temp = finalDirPath + "/"+param.getName();
        String uploadDirPath = StringUtils.substringBefore(temp, ".");
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath,tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        //打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。 创建可读可写的随机访问文件
        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        //获取文件通道
        FileChannel fileChannel = tempRaf.getChannel();

        //写入该分片数据
        long offset = CHUNK_SIZE * param.getChunk();
        byte[] fileData = param.getFile().getBytes();//将文件内容转换为一个字节数组
        //把文件影射为内存映像文件
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        // 释放
        FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        mappedByteBuffer.clear();
        boolean isOk = checkAndSetUploadProgress(param, uploadDirPath);
        System.out.println("========================"+isOk);
        if (isOk) {
            boolean flag = renameFile(tmpFile, fileName);
            if (flag)
                System.out.println("重命名成功了！");
            Md5 byFileNameAndPath = md5Repository.findByFileNameAndPath(fileName, path);
            if (byFileNameAndPath != null){
                File file = new File(uploadDirPath+"/"+fileName);
                Integer tmp;
                String tempDirName = "";
                for (int i=0;i<4;i++) {
                    tmp = new Random().nextInt(10);
                    tempDirName = tempDirName+String.valueOf(tmp);
                }
                fileName = tempDirName+"-"+fileName;
                boolean b = renameFile(file, fileName);
                if (b)
                    System.out.println("副本文件重命名成功！");
            }
            File file = new File(uploadDirPath+"/"+fileName);
            if (file.exists()){
 //               List<Md5> tmps = md5Repository.findByFileMd5AndFileNameAndUsername(param.getMd5(), param.getName(),username);
//                if (tmps.size() > 1){
//                    Integer number = tmps.size();
//                    for (Md5 md5:tmps){
//                        if (number == 1)
//                            break;
//                        else {
//                            md5Repository.deleteById(md5.getId());
//                            number--;
//                        }
//                    }
//                }
//                List<Md5> tmpss = md5Repository.findByFileMd5AndFileNameAndUsername(param.getMd5(), param.getName(),username);
//                if (tmpss.size() == 1){
//                    String uploadPath = "";
//                    for (Md5 md5:tmpss){
//                        uploadPath = md5.getPath();
//                    }
                    System.out.println("上传到hdfs上的地址是："+path);
                    uploadToHdfsService.uploadHdfs(uploadDirPath+"/"+fileName,path);
                    Md5 md51 = new Md5();
                    md51.setUid(param.getUid());
                    md51.setUsername(username);
                    md51.setFileName(fileName);
                    md51.setFileMd5(param.getMd5());
                    md51.setPath(path);
                    md51.setCreateTime(new Date());
                    md5Repository.save(md51);
                    deleteDirectory(uploadDirPath);
//                }
//                else
//                    System.out.println("该文件可能已上传过了，按规定不应该再上传了 ");
            }
            else
                System.out.println("文件出错了！");

            System.out.println("upload complete !!" + flag + " name=" + fileName);
        }

    }


   /**
    * @Author: jitdc
    * @Date: 16:16 2018/7/10
    * @Description: 检查并修改文件上传进度
    */
    private boolean checkAndSetUploadProgress(MultipartFileParam param, String uploadDirPath) throws IOException {
        String fileName = param.getName();
        File confFile = new File(uploadDirPath, fileName + ".conf");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
        //把该分段标记为 true 表示完成
        System.out.println("set part " + param.getChunk() + " complete");
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
            System.out.println("check part " + i + " complete?:" + completeList[i]);
        }

        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(),"true");
            //向redis里存入数据和设置缓存时间
            stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName);
            return true;
        } else {
            if (!stringRedisTemplate.opsForHash().hasKey(Constants.FILE_UPLOAD_STATUS, param.getMd5())) {
                stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(), "false");
            }
            if (!stringRedisTemplate.hasKey(Constants.FILE_MD5_KEY + param.getMd5())) {
                stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName + ".conf");
            }
            return false;
        }

    }

    /**
     * @Author: jitdc
     * @Date: 16:16 2018/7/10
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @Description: 文件重命名
     */
    public boolean renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            logger.info("File does not exist: " + toBeRenamed.getName());
            return false;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        return toBeRenamed.renameTo(newFile);
    }


    /**
     * 删除目录以及目录下的文件
     * @param   sPath 被删除目录的路径
     * @return  目录删除成功返回true，否则返回false
     */
    @Override
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 删除单个文件
     * @param   sPath 被删除文件path
     * @return 删除成功返回true，否则返回false
     */
    @Override
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

}

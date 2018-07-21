package com.example.cloudstore.service.impl;

import com.example.cloudstore.domain.JsonResult;
import com.example.cloudstore.domain.Md5;
import com.example.cloudstore.domain.entity.Dir;
import com.example.cloudstore.repository.Md5Repository;
import com.example.cloudstore.service.MainService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.SchemaOutputResolver;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MainServiceImpl implements MainService {

    @Value("${HDFS_PATH}")
    private String HDFS_PATH;

    @Value("${DA_PATH}")
    private String dataLocalName;
    @Autowired
    private Md5Repository md5Repository;

    @Override
    public JsonResult mkdirMulu(String pPath, String fileName) throws URISyntaxException{
        //拼接成创建文件夹的hdfs全路径
        String muluName = pPath +"/"+ fileName;
        System.out.println("Path:"+pPath+" "+"filename:"+fileName);
        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        Path muluPath = new Path(muluName);
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try {
            fileSystem = FileSystem.get(uri, conf);
            if (fileSystem.exists(muluPath)) {
                result.setStatus("文件已存在！");
            } else {
                fileSystem.mkdirs(muluPath);
                result.setStatus("创建成功");
                Md5 md51 = new Md5();
                md51.setUid("文件夹");
                md51.setFileName(fileName);
                md51.setFileMd5("");
                md51.setPath(pPath);
                md51.setCreateTime(new Date());
                md5Repository.save(md51);
            }
            result.setResult(muluName);
        } catch (IOException e) {
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("创建失败！");
        }

        System.out.println(result.getStatus() + ": " + result.getResult());
        return result;
    }

    @Override
    public JsonResult lookdir(String muluName) throws URISyntaxException {
        System.out.println("muluName:"+muluName);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        Path muluPath = new Path(muluName);
        List<Dir> dirs = new ArrayList<>();
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try {
            fileSystem = FileSystem.get(uri, conf);
            if (!fileSystem.exists(muluPath)) {
                result.setStatus("目录不存在！");
            } else {
                FileStatus[] listStatus = fileSystem.listStatus(muluPath);
                for (FileStatus fileStatus : listStatus) { // 遍历数组
                    Dir dir = new Dir();
                    String isDir = fileStatus.isDirectory() ? "文件夹" : "文件";
                    long len = fileStatus.getLen();
                    dir.setLen(len);
                    Long K = len / 1024 ;
                    Long M = K / 1024;
                    Long G = M / 1024;
                    String path = fileStatus.getPath().toString();
                    int i1 = path.indexOf("/");
                    int i2 = path.indexOf("/",i1+1);
                    int i3 = path.indexOf("/",i2+1);
                    String Path = "/" + path.substring(i3+1,path.length());
                    Long time = fileStatus.getModificationTime();
                    String fileName = fileStatus.getPath().getName();
                    Date date = new Date(time);
                    String Time = formatter.format(date);
                    dir.setIsDir(isDir);
                    if (K<1){dir.setSize(len+" B");}
                    if(K>=1 && K<1024) {dir.setSize(K+" KB");}
                    if(K>=1024&&M<1024){dir.setSize(M+" MB");}
                    if(K>=1024 && M>=1024 && G<1024) {dir.setSize(G+" GB");}
                    if(G>=1024){dir.setSize(G+" GB");}
                    String type = "";
                    String fileType = "";
                    if(isDir.equals("文件夹")){
                        type  = "folder";
                        dir.setType(type);
                    }else{
                        type = fileName.substring(fileName.lastIndexOf(".")+1);
                        fileType = Type(type);
                        dir.setType(fileType);
                    }
                    dir.setPath(Path);
                    dir.setTime(Time);
                    dir.setFileName(fileName);
                    System.out.println(isDir + "\t" + len + "\t" + Path + "\t" + fileName + "\t" + Time + "\t" + type+"|"+fileType);
                    dirs.add(dir);
                }
                result.setStatus("查看成功");
            }
            result.setResult(dirs);
        } catch (IOException e) {
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("创建失败！");
        }
        System.out.println(result.getStatus() + ": " + result.getResult());
        return result;
    }

    @Override
    public JsonResult rename(String oldPath, String newName) throws URISyntaxException {
        System.out.println("oldpath:"+oldPath+" "+"newName:"+newName);
        String oldFileName = oldPath.substring(oldPath.lastIndexOf("/")+1);
        String oldFatherName = oldPath.substring(0,oldPath.length()-oldFileName.length());
        String oldFatherPath = oldPath.substring(0,oldPath.length()-oldFileName.length()-1);
        String newPath = oldFatherName+newName;

        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        Path oldHdfsPath = new Path(oldPath);
        Path newHdfsPath = new Path(newPath);
        FileSystem fileSystem;
        try {
            fileSystem = FileSystem.get(uri, conf);
            if (fileSystem.exists(newHdfsPath)) {
                result.setStatus("文件已经存在！");
            } else{
                fileSystem.rename(oldHdfsPath,newHdfsPath);
                result.setStatus("修改成功");
                //向数据库中存入重命名后的文件信息
                //判断是不是文件夹
                Md5 IsDir = md5Repository.findByUidAndPathAndFileName("文件夹",oldFatherPath,oldFileName);
                if (IsDir != null){
                    //修改数据库中文件夹的名字
                    int i = md5Repository.updateDirName(newName,"文件夹",oldFatherPath,oldFileName);
                    //修改文件夹下所有文件的路径名
                    List<Md5> byPathLike = md5Repository.findByPathLike(oldPath);//找出所有在该文件夹下的文件
                    for (Md5 md5:byPathLike){
                        String newFilePath  = newPath+ md5.getPath().substring(oldPath.length());
                        md5Repository.updateFilePathInDirNameLike(newFilePath,md5.getPath());
                    }

                }
                else {
                    //修改数据库中对于数据的filename
                    int i = md5Repository.updateFileName(newName, oldFatherPath, oldFileName);
                }
            }
            result.setResult(newName.substring(newName.lastIndexOf("/") + 1));
        } catch (IOException e) {
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("修改失败！");
        }
        System.out.println(result.getStatus() + "，已经修改为： " + result.getResult());
        return result;
    }

    @Override
    public List<JsonResult> deleteHDFS(String[] fileDelPaths) throws URISyntaxException {
        List<JsonResult> results = new ArrayList<>();

        for (String fileDelPath : fileDelPaths) {
            JsonResult jsonResult = new JsonResult();

            String deleteName = fileDelPath;
            Path deletePath = new Path(deleteName);
            Configuration conf = new Configuration();
            URI uri = new URI(HDFS_PATH);
            FileSystem fileSystem;
            try {
                fileSystem = FileSystem.get(uri, conf);
                if (!fileSystem.exists(deletePath)) {
                    jsonResult.setStatus("文件不存在");
                } else {
                    fileSystem.delete(deletePath, true);
                    jsonResult.setStatus("删除成功！");
                }
                jsonResult.setResult(deleteName);
            } catch (IOException e) {
                e.printStackTrace();
                jsonResult.setResult(e.getMessage());
                jsonResult.setStatus("删除失败！");
            }
            System.out.println(jsonResult.getStatus() + ": " + jsonResult.getResult());
            results.add(jsonResult);
        }
        return results;
    }

    @Override
    public List<JsonResult> move(String[] oldDirPaths, String newFatherPath) throws URISyntaxException {
        List<JsonResult> results = new ArrayList<>();
        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        for (String oldDirPath : oldDirPaths) {
            JsonResult result = new JsonResult();
            String oldname = oldDirPath.substring(oldDirPath.lastIndexOf("/") + 1); // test2
            String oldFatherPath = oldDirPath.substring(0,oldDirPath.length()-oldname.length()-1);
			 /*文件的新hdfs全路径*/
            String newDirPath = newFatherPath + "/" + oldname; //   /zlw/test1/test2

            Path newPath = new Path(newDirPath); // /zlw/test1/test2
            Path newfatherPath = new Path(newFatherPath); // zlw/test1
            Path oldHdfsPath = new Path(oldDirPath); //文件的旧全路径 /zlw/test2
            FileSystem fileSystem;
            try {
                fileSystem = FileSystem.get(uri, conf);
                if (!fileSystem.exists(newfatherPath)) {
                    result.setStatus("移动失败");
                    result.setResult("新文件夹不存在！");
                } else if (newFatherPath.equals(oldDirPath) || newFatherPath.startsWith(oldDirPath.substring(0,oldDirPath.length()))) {
                    result.setStatus("移动失败");
                    result.setResult(newDirPath.substring(newDirPath.lastIndexOf("/")+1)+"不能移动到本文件夹或其子文件夹下");
                } else if (fileSystem.exists(newPath)) {
                    result.setStatus("移动失败");
                    result.setResult("该目录下已经存在"+newDirPath.substring(newDirPath.lastIndexOf("/")+1));
                } else {
                    fileSystem.rename(oldHdfsPath, newPath);
                    result.setStatus("移动成功");
                    result.setResult(result.getStatus());
                    //先判断当前路径是否是文件夹
                    Md5 IsDir = md5Repository.findByUidAndPathAndFileName("文件夹", oldFatherPath,oldname);
                    if (IsDir != null){
                        //是文件夹，修改文件夹的路径
                        int i = md5Repository.updateDirPath(newFatherPath,"文件夹",oldFatherPath,oldname);
                        //修改文件夹下所有文件的路径名
                        List<Md5> byPathLike = md5Repository.findByPathLike(oldDirPath);//找出所有在该文件夹下的文件
                        for (Md5 md5:byPathLike){
                            String newFilePath  = newFatherPath+ md5.getPath().substring(oldFatherPath.length());
                            md5Repository.updateFilePathInDirNameLike(newFilePath,md5.getPath());
                        }

                    }
                    else {//是文件，修改该条数据的路径
                        int i = md5Repository.updateFilePath(newFatherPath, oldFatherPath, oldname);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                result.setResult(e.getMessage());
                result.setStatus("移动失败！");
            }
            results.add(result);


        }
        return results;
    }

    @Override
    public ResponseEntity<JsonResult> upload(MultipartFile file, String mulupath) throws URISyntaxException {
        JsonResult result = new JsonResult();
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                //存储文件的hdfs全路径
                String srcName = mulupath + "/" + fileName;

                // 如果用户目录不存在，则在服务器中创建
                String userFatherFileName = srcName.split("/")[1];
                System.out.println(userFatherFileName);
                File userFolder = new File(dataLocalName+userFatherFileName);
                if (!userFolder.exists()) {
                    userFolder.mkdir();
                }
                // 将文件上传到虚拟机服务器
                FileOutputStream targetFileStream = new FileOutputStream(userFolder + "/" + fileName);
                BufferedOutputStream out = new BufferedOutputStream(targetFileStream);
                out.write(file.getBytes());
                out.flush();
                out.close();
                // 将文件上传到HDFS
                File[] files = userFolder.listFiles();
                for (File targetFile : files) {
                    if (targetFile.getName().equals(fileName)) {
                        result = uploadHDFS(targetFile,srcName,mulupath);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result.setResult(e.getMessage());
                result.setStatus("上传到服务器失败！");
            } catch (IOException e) {
                e.printStackTrace();
                result.setResult(e.getMessage());
                result.setStatus("上传到服务器失败！");
            }
        } else {
            result.setResult(null);
            result.setStatus("文件不能为空！");
        }
        return ResponseEntity.ok(result);
    }

    public String Type(String type) {
        if(type.equals("jpg")||type.equals("png")||type.equals("gif")||type.equals("gpeg")||type.equals("pic")||type.equals("bmp")){
            return "img";
        }
        if (type.equals("zip")|| type.equals("rar")||type.equals("gz")){
            return "zip";
        }
        if (type.equals("avi")|| type.equals("mov") || type.equals("swf")|| type.equals("mpg")|| type.equals("mp4")){
            return "video";
        }
        if (type.equals("c")|| type.equals("java")|| type.equals("py")|| type.equals("cpp")|| type.equals("vue")){
            return "code";
        }
        if (type.equals("doc")|| type.equals("docx")){
            return "doc";
        }
        if (type.equals("ppt")|| type.equals("pptx")){
            return "ppt";
        }
        if (type.equals("xls")|| type.equals("xlsx")){
            return "xls";
        }
        if (type.equals("wmv")|| type.equals("aif")||type.equals("mp3")|| type.equals("au")||type.equals("ram")|| type.equals("wam")){
            return "music";
        }
        if (type.equals("txt")){
            return "txt";
        }
        if (type.equals("pdf")){
            return "pdf";
        }
        return "others";
    }

    // 将文件上传到HDFS
    private JsonResult uploadHDFS(File file, String srcName, String mulu) throws URISyntaxException {
        JsonResult result = new JsonResult();

        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try {
            fileSystem = FileSystem.get(uri, conf);
            // 如果用户目录不存在，则在HDFS中创建
            Path hdfsFoler = new Path(mulu);
            if (!fileSystem.exists(hdfsFoler)) {
                fileSystem.mkdirs(hdfsFoler);
            }
            Path hdfsFile = new Path(srcName);
            if (fileSystem.exists(hdfsFile)) {
                result.setStatus("文件已存在！");
            } else {
                Path localFile = new Path(file.getAbsolutePath());
                fileSystem.copyFromLocalFile(true, true, localFile, hdfsFile);
                //fileService.mkdirFile(id,srcName);
                result.setStatus("上传成功！");
                file.delete();
            }
            result.setResult(hdfsFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("上传到HDFS失败！");
        }

        System.out.println(result.getStatus() + ": " +result.getResult());
        return result;
    }

}

package com.example.cloudstore.controller;


import com.example.cloudstore.domain.JsonResult;
import com.example.cloudstore.domain.entity.Dir;
import com.example.cloudstore.domain.entity.JsonDelete;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MainController {

	@Value("${HDFS_PATH}")
	private String HDFS_PATH;
	
	
	private String dataLocalName ="E:\\IDEA_FILE\\zhuoyun\\cloudstore\\src\\main\\java\\com\\example\\cloudstore\\data\\";

	//上传文件(TODO)
	@ApiOperation(value = "上传文件到Hadoop文件系统", notes = "上传文件的文件名")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	//先上传到虚拟机服务器上
	public ResponseEntity<JsonResult> upload(@RequestParam("file") MultipartFile file, @RequestParam String mulupath) throws URISyntaxException {
		JsonResult result = new JsonResult();
		if (!file.isEmpty()) {
			try {
				String fileName = file.getOriginalFilename();
				//String mulupath = fileService.findFile(id).getFilepath();
				//	System.out.println(mulupath);
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
		// 将文件上传到HDFS
	private JsonResult uploadHDFS(File file,String srcName,String mulu) throws URISyntaxException {
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


	//删除文件（已完成）//todo 有疑问，只是传递了字符串。
	@ApiOperation(value = "删除文件", notes = "文件全路径")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public List<JsonResult> deleteHDFS(@RequestParam String[] fileDelPaths) throws URISyntaxException {
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


	//创建文件夹(已完成)
	@ApiOperation(value = "创建文件夹", notes = "文件全路径")
	@RequestMapping(value = "/mkdir", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pPath", value = "创建该文件的父目录全路径", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "fileName", value = "创建该文件的文件名", required = true, dataType = "String", paramType = "query"),
	})
	public JsonResult mkdirMulu(@RequestParam String pPath, @RequestParam String fileName) throws URISyntaxException {
		//获取该文件父目录的信息
	//	Fdir fdir = fileService.findFile(id);
		//获取父目录的全路径
	//	String pPath = fdir.getFilepath();

		//拼接成创建文件夹的hdfs全路径
		String muluName = pPath +"/"+ fileName;
		System.out.println("Path:"+pPath+" "+"filename:"+fileName);
		//在数据表里添加数据，下表中的id，其实
	//	fileService.mkdirFile(id, muluName);

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


	//递归目录（已完成）
	@ApiOperation(value = "查看文件夹", notes = "文件全路径")
	@RequestMapping(value = "/dirLook", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "muluName", value = "要查看文件夹的全路径", required = true, dataType = "String", paramType = "query"),
	})
	public JsonResult lookdir(@RequestParam String muluName) throws URISyntaxException {
	//	Fdir fdir = fileService.findFile(id);
	//	String muluName = fdir.getFilepath();
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
					if(K<1024) {dir.setSize(K+" KB");}
					if(K>=1024&&M<1024){dir.setSize(M+" MB");}
					if(K>=1024 && M>=1024 && G<1024) {dir.setSize(G+" GB");}
					if(G>=1024){dir.setSize(G+" GB");}
					String type = "";
					if(isDir.equals("文件夹")){
						type  = "folder";
						dir.setType(type);
					}else{
						type = fileName.substring(fileName.lastIndexOf(".")+1);
						dir.setType(type);
					}
					dir.setPath(Path);
					dir.setTime(Time);
					dir.setFileName(fileName);
					System.out.println(isDir + "\t" + len + "\t" + Path + "\t" + fileName + "\t" + Time +"\t"+type);
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


	//重命名文件夹（已完成）
	@ApiOperation(value = "重命名文件夹", notes = "新旧文件夹的全路径")
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oldPath", value = "hdfs文件的旧路径", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "newName", value = "文件的新的名字", required = true, dataType = "String", paramType = "query"),
	})
	public JsonResult rename(@RequestParam String oldPath, @RequestParam String newName) throws URISyntaxException{
		//Fdir fdir = fileService.findFile(id);
		//获取原来的名字
		//String oldPath = fdir.getFilepath();
		//Fdir fdir1 = fileService.findFile(fdir.getPid());
		//拼接成新的hdfs新路径
		//String newPath = fdir1.getFilepath()+"/"+newName;
		//
		System.out.println("oldpath:"+oldPath+" "+"newName:"+newName);
		String oldFileName = oldPath.substring(oldPath.lastIndexOf("/")+1);
		String oldFatherName = oldPath.substring(0,oldPath.length()-oldFileName.length());
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
			//	fileService.setFile(id,fdir.getPid(),newPath);
				result.setStatus("修改成功");
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

	//移动文件夹(已完成)
	@ApiOperation(value = "移动文件夹", notes = "新旧文件夹的全路径")
	@RequestMapping(value = "/move", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oldDirPath", value = "要移动的文件的路径", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "newFatherPath", value = "要移动到的文件夹的路径", required = true, dataType = "String", paramType = "query"),
	})
	public JsonResult move(@RequestParam String oldDirPath , @RequestParam String newFatherPath) throws URISyntaxException{
		JsonResult result = new JsonResult();
		Configuration conf = new Configuration();
		URI uri = new URI(HDFS_PATH);
		String oldname = oldDirPath.substring(oldDirPath.lastIndexOf("/")+1); // test2
		// 文件的新hdfs全路径
		String newDirPath = newFatherPath +"/" + oldname; //   /zlw/test1/test2

		Path newPath = new Path(newDirPath); // /zlw/test1/test2
		Path newfatherPath = new Path(newFatherPath); // zlw/test1
		Path oldHdfsPath = new Path(oldDirPath); //文件的旧全路径 /zlw/test2
		FileSystem fileSystem;

		try {
			fileSystem = FileSystem.get(uri, conf);
			if (!fileSystem.exists(newfatherPath)) {
				result.setStatus("移动失败");
				result.setResult("新文件夹不存在！");
			}else if(newFatherPath.equals(oldDirPath)){
				result.setStatus("移动失败");
				result.setResult("不能移动到本文件夹");
			} else if(fileSystem.exists(newPath)){
				result.setStatus("移动失败");
				result.setResult("该文件下已经存在相同名字的文件");
			} else{
					fileSystem.rename(oldHdfsPath,newPath);
					result.setStatus("移动成功");
					result.setResult(result.getStatus() + "，已经移动到： 文件夹" + newDirPath +"中");
			}
		} catch (IOException e) {
			e.printStackTrace();
			result.setResult(e.getMessage());
			result.setStatus("移动失败！");
		}
		System.out.println(result);
		return result;
	}
}

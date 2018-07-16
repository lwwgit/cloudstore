package com.example.cloudstore.controller;


import com.example.cloudstore.domain.JsonResult;
import com.example.cloudstore.domain.entity.Dir;
import com.example.cloudstore.domain.entity.JsonDelete;
import com.example.cloudstore.service.MainService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private MainService mainService;

	@ApiOperation(value = "普通测试上传文件到Hadoop文件系统", notes = "上传文件的文件名")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<JsonResult> upload(@RequestParam("file") MultipartFile file, @RequestParam String mulupath) throws URISyntaxException {
		return mainService.upload(file, mulupath);
	}

	@ApiOperation(value = "创建文件夹", notes = "文件全路径")
	@RequestMapping(value = "/mkdir", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pPath", value = "创建该文件的父目录全路径", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "fileName", value = "创建该文件的文件名", required = true, dataType = "String", paramType = "query"),
	})
	public JsonResult mkdirMulu(@RequestParam String pPath, @RequestParam String fileName) throws URISyntaxException {
		return mainService.mkdirMulu(pPath,fileName);
	}

	@ApiOperation(value = "递归查看文件夹", notes = "文件全路径")
	@RequestMapping(value = "/dirLook", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "muluName", value = "要查看文件夹的全路径", required = true, dataType = "String", paramType = "query"),
	})
	public JsonResult lookdir(@RequestParam String muluName) throws URISyntaxException {
		return mainService.lookdir(muluName);
	}

	@ApiOperation(value = "重命名文件夹", notes = "新旧文件夹的全路径")
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oldPath", value = "hdfs文件的旧路径", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "newName", value = "文件的新的名字", required = true, dataType = "String", paramType = "query"),
	})
	public JsonResult rename(@RequestParam String oldPath, @RequestParam String newName) throws URISyntaxException{
		return mainService.rename(oldPath, newName);
	}

	@ApiOperation(value = "删除文件", notes = "文件全路径")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public List<JsonResult> deleteHDFS(@RequestParam String[] fileDelPaths) throws URISyntaxException {
		return mainService.deleteHDFS(fileDelPaths);
	}

	@ApiOperation(value = "移动文件夹", notes = "新旧文件夹的全路径")
	@RequestMapping(value = "/move", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oldDirPaths", value = "要移动的文件的路径", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "newFatherPath", value = "要移动到的文件夹的路径", required = true, dataType = "String", paramType = "query"),
	})
	public List<JsonResult> move(@RequestParam String[] oldDirPaths , @RequestParam String newFatherPath) throws URISyntaxException{
		return mainService.move(oldDirPaths, newFatherPath);
	}


}

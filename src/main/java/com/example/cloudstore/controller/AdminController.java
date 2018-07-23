package com.example.cloudstore.controller;


import com.example.cloudstore.domain.JsonResult;
import com.example.cloudstore.domain.JsonUser2Adm;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.service.AdminService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
public class AdminController {

    @Value("${HDFS_PATH}")
    private String HDFS_PATH;


    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "查询指定用户", notes = "查询指定用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/specialDisplay")
    public JsonResult specialDisplay(@RequestParam String username) throws URISyntaxException{
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("查找成功");
        jsonResult.setResult(adminService.specificDisply(username));
        return jsonResult;
    }

    @ApiOperation(value = "(分页)登录上来就可以查看到的用户列表", notes = "用户的基本信息和文件夹")
    @PostMapping("/infoDisplayPage")
    public JsonResult InfoDisplay(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        JsonResult jsonResult = new JsonResult();
        PageRequest pageable =  PageRequest.of(currentPage,pageSize);
        jsonResult.setResult(adminService.UserDisplay(pageable));
        return jsonResult;
    }

    @ApiOperation(value = "登录上来就可以查看到的用户列表", notes = "用户的基本信息和文件夹")
    @PostMapping("/infoDisplay")
    public JsonResult InfoDisplay() throws URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResult(adminService.InfoDisplay());
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }

    @ApiOperation(value = "查看用户详细信息", notes = "")
    @PostMapping("/userInfoDisplay")
    public JsonResult UserInfoDisplay(@RequestParam String username){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResult(adminService.userInfoDisplay(username));
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }



    @ApiOperation(value = "查看vip用户信息", notes = "vip用户的基本信息和文件夹")
    @PostMapping("/vipInfoDisplay")
    public JsonResult VipInfoDisplay() throws IOException,URISyntaxException{
        JsonResult jsonResult = new JsonResult();
        List<JsonUser2Adm> jsonUser2Adms= adminService.vipInfoDisPlay();
        jsonResult.setResult(jsonUser2Adms);
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }

    @ApiOperation(value = "查看非vip用户信息", notes = "非vip用户的基本信息和文件夹")
    @PostMapping("/noVipInfoDisplay")
    public JsonResult NoVipInfoDisplay() throws IOException,URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        List<JsonUser2Adm> jsonUser2Adms= adminService.novipInfoDisPlay();
        jsonResult.setResult(jsonUser2Adms);
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }

    @ApiOperation(value = "设置vip用户", notes = "给用户设置vip权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/tobeVip")
    public int tobeVip(@RequestParam String username){
        return adminService.tobeVip(username);
    }

    @ApiOperation(value = "取消vip用户", notes = "给用户取消vip权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/cancelVip")
    public int cancelVip(@RequestParam String username){
        return adminService.cancleVip(username);
    }


    @ApiOperation(value = "启用用户状态", notes = "给用户启用状态，用户正常使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/modeEnable")
    public int modeEnable(@RequestParam String username){
        return adminService.modeEnable(username);
    }

    @ApiOperation(value = "冻结用户状态", notes = "给用户冻结状态，用户不能正常使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/modeFreeze")
    public int modeFreeze(@RequestParam String username){
        return adminService.modeFreeze(username);
    }


    @ApiOperation(value = "用户提交申诉状态", notes = "提交申诉，改变申诉状态，添加申诉信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cominfo", value = "申诉信息", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/comSub")
    public String comSub(@RequestParam String username, @RequestParam String cominfo){
        return adminService.comSub(username, cominfo);
    }

    @ApiOperation(value = "管理员查看申诉信息", notes = "提交申诉，改变申诉状态，添加申诉信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/comView")
    public JsonResult comSub(@RequestParam String username){
        JsonResult Result = new JsonResult();
        Result.setStatus("查看成功");
        Result.setResult(adminService.comView(username));
        return Result;
    }

    @ApiOperation(value = "每天的0点清空这个文件夹", notes = "清空文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "路径", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/timedEmptying")
    public void TimedEmptying(@RequestParam String path) {
        final long PERIOD_DAY = 24 * 60 * 60 * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date=calendar.getTime();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Configuration conf=new Configuration();
                try{
                    URI uri = new URI(HDFS_PATH);
                    FileSystem hdfs = FileSystem.get(uri,conf);
                    Path delef = new Path(path);
                    //递归删除
                    hdfs.delete(delef,true);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }, date.getTime(),PERIOD_DAY);
}


    @ApiOperation(value = "年龄分布数据", notes = "Echart所需数据")
    @PostMapping("/ageDistribution")
    public JsonResult ageDistribution(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("获得数据成功");
        jsonResult.setResult(adminService.ageDistribution());
        return jsonResult;
    }

    @ApiOperation(value = "地区分布数据", notes = "Echart所需数据")
    @PostMapping("/areaDistribution")
    public JsonResult areaDistribution(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("获得数据成功");
        jsonResult.setResult(adminService.areaData());
        return jsonResult;
    }

    @PostMapping("/areaData")
    public JsonResult areaData(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("获得数据成功");
        jsonResult.setResult(adminService.areaData());
        return jsonResult;
    }


    @ApiOperation(value = "使用情况", notes = "")
    @PostMapping("/capacity")
    public JsonResult Capacity(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("获得数据成功");
        jsonResult.setResult(adminService.cap());
        return jsonResult;
    }

    @PostMapping("/userReport")
    public JsonResult userReport(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("获得数据成功");
        jsonResult.setResult(adminService.userReport());
        return jsonResult;
    }

    @PostMapping("/countData")
    public JsonResult countData(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("获得数据成功");
        jsonResult.setResult(adminService.CountData());
        return jsonResult;
    }

    @PostMapping("/subFailure")
    public JsonResult subFailure(String username){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("修改成功");
        jsonResult.setResult(adminService.subFailure(username));
        return jsonResult;
    }

}

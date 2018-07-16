package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.service.IconService;
import com.example.cloudstore.service.SysUserService;
import com.example.cloudstore.service.UserInfoService;
import com.example.cloudstore.service.impl.SmsService;
import com.example.cloudstore.utils.ResultUtil;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private IconService iconService;
    @Autowired
    private SmsService smsService;


    /**
     * 忘记密码
     */
    @PostMapping("/forget/password")
    public Result  forgetPassword(String newPassword,String tel){
         SysUser sysUser = userService.selectByTel(tel);
         if(sysUser == null){
             return ResultUtil.error(1,"用户不存在");
         }
         sysUser.setPassword(passwordEncoder.encode(newPassword));
         userService.update(sysUser);
         return ResultUtil.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/change/password")
    public Result  changePassword(String newPassword){
        String currentUsername = globalFunction.getUsername();
        SysUser sysUser = userService.selectByName(currentUsername);
        sysUser.setPassword(passwordEncoder.encode(newPassword));
        userService.update(sysUser);
        return ResultUtil.success();
    }

    /**
     * 修改手机号
     */
    @PutMapping("/change/tel")
    public Result  changeTel(HttpServletRequest request, String newTel, String sms){
        if(userService.selectByTel(newTel) != null){
            return ResultUtil.error(1,"手机号已被注册");
        }
        boolean result = smsService.checkSmsCode(request,sms);
        if (result == true) {
            String currentUsername = globalFunction.getUsername();
            SysUser sysUser = userService.selectByName(currentUsername);
            sysUser.setTel(newTel);
            userService.update(sysUser);
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"验证码错误");
        }
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/user/info")
    public Result userInfo(UserInfo userInfo){
        String currentUsername = globalFunction.getUsername();
        UserInfo info = userInfoService.findByUsername(currentUsername);
        info.setCity(userInfo.getCity());
        info.setAge(userInfo.getAge());
        info.setIntroduction(userInfo.getIntroduction());
        info.setSex(userInfo.getSex());
        userInfoService.update(info);
        return ResultUtil.success();
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/get/user/info")
    public Result getUserInfo(){
        String currentUsername = globalFunction.getUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        return ResultUtil.success(userInfo);
    }

    /**
     * 上传（修改）头像 content-type :form-data
     * @return
     * @throws IOException
     */
    @PostMapping("/user/icon/upload")
    public Result uploadImageFile(String username,MultipartFile file) throws IOException {
//        String username = globalFunction.getUsername();
        System.out.println(username);
        String dst = "/userIcon/" + username + ".jpg";
        if (!file.isEmpty()) {
            try {
                String src = file.getOriginalFilename();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(src)));
                out.write(file.getBytes());
                out.flush();
                out.close();

                String IconPath = iconService.uploadImageFile(src,dst);
                if (IconPath != null) {
                    //插入数据库
                    UserInfo userInfo = userInfoService.findByUsername(username);
                    userInfo.setIcon(IconPath);
                    userInfoService.insert(userInfo);
                    return ResultUtil.success();
                }else {
                    return ResultUtil.error(1,"上传失败");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return ResultUtil.error(1,"上传失败, " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                return ResultUtil.error(1,"上传失败, " + e.getMessage());
            }


        } else {
            return ResultUtil.error(1,"上传失败,文件为空");
        }

    }

    /**
     * 获取头像
     */
    @GetMapping("/user/icon/get")
    public void readFileByAPI(HttpServletResponse response) throws IOException {
//        String iconPath = "hdfs://192.168.59.145:9000/userIcon/lww.jpg";
        String username = globalFunction.getUsername();
        UserInfo userInfo = userInfoService.findByUsername(username);
        String iconPath = userInfo.getIcon();


        //获取文件系统
        FileSystem fs = globalFunction.getHadoopFileSystem();

        //获取路径
        Path p = new Path(iconPath);
        //通过文件系统打开路径获取HDFS文件输入流
        FSDataInputStream fis =  fs.open(p);
        //创建缓冲区
        byte[] buf = new byte[1024*1024];
        int len = -1;
        //当当读取的长度不等于-1的时候开始写入
        //写入需要字节输出流
        OutputStream baos = response.getOutputStream();
        while ((len = fis.read(buf)) != -1){
            baos.write(buf,0,len);
        }
        //写入完毕，关闭输入流
        fis.close();
        //关闭输出流
        baos.close();
    }

}

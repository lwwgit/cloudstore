package com.example.cloudstore.controller;

import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.service.SortService;
import com.example.cloudstore.service.UserInfoService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ImageController {
    @Autowired
    private GlobalFunction globalFunction;

    @Autowired
    private SortService sortService;

    @Autowired
    private UserInfoService userInfoService;

    @Value("${HDFS_PATH}")
    private String HADOOP_URL;

    /**
     * 点击图片预览
     *
     * @param imagePath
     * @param response
     * @throws IOException
     */
    @GetMapping("/usr/img/preview")
    public void imagePreview(String imagePath, HttpServletResponse response) throws IOException {

        String filename = HADOOP_URL + imagePath;
        //获取文件系统
        FileSystem fs = globalFunction.getHadoopFileSystem();

        //获取路径
        Path p = new Path(filename);
        //通过文件系统打开路径获取HDFS文件输入流
        FSDataInputStream fis = fs.open(p);
        //创建缓冲区
        byte[] buf = new byte[fis.available()];
        int len = -1;
        //当当读取的长度不等于-1的时候开始写入
        //写入需要字节输出流
        OutputStream baos = response.getOutputStream();
        while ((len = fis.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        //写入完毕，关闭输入流
        fis.close();
        //关闭输出流
        baos.close();
    }

    /**
     * 获取图片列表
     *
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @GetMapping("/usr/img/line")
    public List<Map<String, Object>> SortFile() throws IOException, URISyntaxException {

        FileSystem fs = globalFunction.getHadoopFileSystem();
        // 读取图片字节数组
        List<Map<String, Object>> dataList = new ArrayList<>();

        List<Map<String, Object>> list = sortService.SortFile(2);
        //取出path所有值
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String imgPath = map.get("path").toString();
            //获取路径
            Path p = new Path(HADOOP_URL + imgPath);
            //通过文件系统打开路径获取HDFS文件输入流
            FSDataInputStream in = fs.open(p);
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            map.put("base64", encoder.encode(data));
            dataList.add(map);
        }
        return dataList;
    }


    /**
     * 预览音频和视频
     *
     * @param fpath
     * @param req
     * @param resp
     * @throws IOException
     */
    @GetMapping("/get/stream")
    public void preview(String fpath, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (fpath == null)
            return;
        String filename = HADOOP_URL + fpath;
        Configuration config = new Configuration();
        FileSystem fs = null;
        FSDataInputStream in = null;
        try {
            fs = FileSystem.get(URI.create(filename), config);
            in = fs.open(new Path(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final long fileLen = fs.getFileStatus(new Path(filename)).getLen();
        String range = req.getHeader("Range");
        resp.setHeader("Content-type", "video/mp3");
        OutputStream out = resp.getOutputStream();
        if (range == null) {
            filename = fpath.substring(fpath.lastIndexOf("/") + 1);
            resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
            resp.setContentType("application/octet-stream");
            resp.setContentLength((int) fileLen);
            IOUtils.copyBytes(in, out, fileLen, false);
        } else {
            long start = Integer.valueOf(range.substring(range.indexOf("=") + 1, range.indexOf("-")));
            long count = fileLen - start;
            long end;
            if (range.endsWith("-"))
                end = fileLen - 1;
            else
                end = Integer.valueOf(range.substring(range.indexOf("-") + 1));
            String ContentRange = "bytes " + String.valueOf(start) + "-" + end + "/" + String.valueOf(fileLen);
            resp.setStatus(206);
            resp.setContentType("video/mpeg3");
            resp.setHeader("Content-Range", ContentRange);
            in.seek(start);
            try {
                IOUtils.copyBytes(in, out, count, false);
            } catch (Exception e) {
                throw e;
            }
        }
        in.close();
        out.close();
    }

    /**
     * 获取其他用户的头像
     */
    @GetMapping("/icon/get")
    public void getIcon(HttpServletResponse response, String username) throws IOException {
        System.out.println("icon Username" + username);
        UserInfo userInfo = userInfoService.findByUsername(username);
        String iconPath = userInfo.getIcon();
        if (iconPath == null){
            iconPath = HADOOP_URL+ "userIcon/default.jpg";
        }
        System.out.println("iconPath: " + iconPath);
        //获取文件系统
        FileSystem fs = globalFunction.getHadoopFileSystem();

        //获取路径
        Path p = new Path(iconPath);
        //通过文件系统打开路径获取HDFS文件输入流
        FSDataInputStream fis = fs.open(p);
        //创建缓冲区
        byte[] buf = new byte[1024 * 1024];
        int len = -1;
        //当当读取的长度不等于-1的时候开始写入
        //写入需要字节输出流
        OutputStream baos = response.getOutputStream();
        while ((len = fis.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        //写入完毕，关闭输入流
        fis.close();
        //关闭输出流
        baos.close();
    }

}

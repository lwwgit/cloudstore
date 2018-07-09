package com.example.cloudstore.controller;

import com.example.cloudstore.service.SortService;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImageController {
    @Autowired
    private GlobalFunction globalFunction;

    @Autowired
    private SortService sortService;
    /**
     * 点击图片预览
     * @param imagePath
     * @param response
     * @throws IOException
     */
    @GetMapping("/usr/image/preview")
    public void imagePreview(String imagePath,HttpServletResponse response) throws IOException {

        //获取文件系统
        FileSystem fs = globalFunction.getHadoopFileSystem();

        //获取路径
        Path p = new Path(imagePath);
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

    /**
     * 获取图片列表
     * [{path : /lww/test1.jpg, base64:124wefrdnzsbfgvjm },
     *  {path : /lww/test2.jpg, base64:124wefrdnzsbfgvjm}]
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @GetMapping("/usr/image/line")
    public List<Map<String,Object>> SortFile() throws IOException, URISyntaxException {

        // 读取图片字节数组
        List<Map<String, Object>> dataList = new ArrayList<>();


        List<Map<String,Object>> list = sortService.SortFile(2);
        List<String> paths = new ArrayList<>();
        //取出path所有值
        for(int i = 0;i < list.size();i++)
        {
            Map<String,Object> map = list.get(i);
            String imagePath = map.get("Path").toString();
            paths.add(imagePath);
        }
        FileSystem fs = globalFunction.getHadoopFileSystem();
        for (int i = 0; i < paths.size();i++) {

            Map<String, Object> mapList = new HashMap<>();
            //获取路径
            Path p = new Path(paths.get(i));
            //通过文件系统打开路径获取HDFS文件输入流
            FSDataInputStream in = fs.open(p);

            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            mapList.put("path",paths.get(i));
//            System.out.println(paths.get(i));
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            mapList.put("base64",encoder.encode(data));
            dataList.add(mapList);
        }
        return dataList;

    }
}

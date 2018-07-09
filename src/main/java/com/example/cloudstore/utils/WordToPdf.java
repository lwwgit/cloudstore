package com.example.cloudstore.utils;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.apache.hadoop.fs.FSDataInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Word转Pdf
 * @author jitwxs
 * @date 2018/1/6 22:26
 */
public class WordToPdf {
    private static InputStream license;
    private static FSDataInputStream fileInput;
    private static File outputFile;

    /**
     * 主方法
     * @param outputFile 输出文件全路径
     * @return 耗时(s) ，失败返回-1
     */
    public static int run(FSDataInputStream fileInput1, String outputFile) {
        if (!getLicense(outputFile)) {
            return -1;
        }
        try {
            fileInput=fileInput1;
            long old = System.currentTimeMillis();
            Document doc = new Document(fileInput);
            FileOutputStream fileOS = new FileOutputStream(outputFile);

            doc.save(fileOS, SaveFormat.PDF);

            long now = System.currentTimeMillis();
            fileOS.close();
            return (int)((now - old) / 1000.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static boolean getLicense(String output) {
        boolean result = false;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            license = new FileInputStream(loader.getResource("license.xml").getPath());
            outputFile = new File(output);
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
            license.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

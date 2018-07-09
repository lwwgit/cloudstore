package com.example.cloudstore.utils;

import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PptToPdf {
    private static InputStream license;
    private static InputStream slides;

    /**
     * 主方法
     * @param inputFile 输入文件全路径
     * @param outputFile 输出文件全路径
     * @return 耗时(s) ，失败返回-1
     */
    public static int run(String inputFile, String outputFile) {
        // 验证License
        if (!getLicense(inputFile)) {
            return -1;
        }

        try {
            long old = System.currentTimeMillis();
            Presentation pres = new Presentation(inputFile);
            File file = new File(outputFile);
            FileOutputStream fileOS = new FileOutputStream(file);

            pres.save(fileOS, SaveFormat.Pdf);

            long now = System.currentTimeMillis();
            fileOS.close();
            return (int)((now - old) / 1000.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean getLicense(String inputFile ) {
        boolean result = false;
        try {
            license = PptToPdf.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(license);
            license.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

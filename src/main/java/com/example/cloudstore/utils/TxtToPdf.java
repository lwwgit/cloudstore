package com.example.cloudstore.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

public class TxtToPdf {
    public static void txt2pdf(String txtPath, String pdfPath) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);

        InputStream is = new FileInputStream(txtPath);
        //读取文本内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

        /** 新建一个字体,iText的方法
         * STSongStd-Light 是字体，在iTextAsian.jar 中以property为后缀
         * UniGB-UCS2-H   是编码，在iTextAsian.jar 中以cmap为后缀
         * H 代表文字版式是 横版， 相应的 V 代表 竖版
         */
        BaseFont bfChinese = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese, 12,Font.NORMAL);
//		 打开文档，将要写入内容
        document.open();
        String line=reader.readLine();
        while(line!=null){
            Paragraph pg = new Paragraph(line,fontChinese);
            document.add(pg);
            line=reader.readLine();
        }
        document.close();
        reader.close();
        is.close();
    }
}

package com.example.cloudstore.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

public class HDFS_Downloader
{

    //TODO 此处下面需要更改hdfs地址

    public static FileSystem hdfs;

    public static void downloadFile(String srcPath, String dstPath) throws Exception
    {
        FSDataInputStream in = null;
        FileOutputStream out = null;
        try
        {
            in = hdfs.open(new Path(srcPath));
            out = new FileOutputStream(dstPath);
            IOUtils.copyBytes(in, out, 4096, false);
        }
        finally
        {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
    }

    public static void downloadFolder(String srcPath, String dstPath) throws Exception
    {
        File dstDir = new File(dstPath);
        if (!dstDir.exists())
        {
            dstDir.mkdirs();
        }
        FileStatus[] srcFileStatus = hdfs.listStatus(new Path(srcPath));
        Path[] srcFilePath = FileUtil.stat2Paths(srcFileStatus);
        for (int i = 0; i < srcFilePath.length; i++)
        {
            String srcFile = srcFilePath[i].toString();
            int fileNamePosi = srcFile.lastIndexOf('/');
            String fileName = srcFile.substring(fileNamePosi + 1);
            download(srcPath + '/' + fileName, dstPath + '/' + fileName);
        }
    }

    public static void download(String srcPath, String dstPath) throws Exception
    {
        Configuration conf = new Configuration();
        URI uri = new URI("hdfs://192.168.150.134:9000/");
        hdfs = FileSystem.get(uri, conf);
        if (hdfs.isFile(new Path(srcPath)))
        {
            downloadFile(srcPath, dstPath);
        }
        else
        {
            downloadFolder(srcPath, dstPath);
        }
    }

    public static void main(String[] args)
    {
            try
            {
                download("/user/file", "C:\\Users\\zlw\\Desktop\\");
            }
            catch (Exception e)
            {
                System.out.println("Error occured when copy files");
            }
    }
}

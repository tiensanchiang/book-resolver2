package com.github.tiensanqiang.book.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil{

    public static void zip(String zipFileName, String sourceFileName) throws Exception {
        //File zipFile = new File(zipFileName);
        System.out.println("压缩中...");

        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));

        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);

        File sourceFile = new File(sourceFileName);

        //调用函数
        compress(out, bos, sourceFile, null);

        bos.close();
        out.close();
        System.out.println("压缩完成");

    }

    public static void unzip(String zipFilePath, String unzipFilePath) throws Exception
    {
        FileOutputStream fos = null;
        InputStream is = null;
        try
        {
            ZipFile zf = new ZipFile(new File(zipFilePath));
            Enumeration en = zf.entries();
            while (en.hasMoreElements())
            {
                ZipEntry zn = (ZipEntry) en.nextElement();
                if (!zn.isDirectory())
                {
                    is = zf.getInputStream(zn);
                    File f = new File(unzipFilePath + zn.getName());
                    File file = f.getParentFile();
                    file.mkdirs();
                    fos = new FileOutputStream(unzipFilePath + zn.getName());
                    int len = 0;
                    byte bufer[] = new byte[10*1024];
                    while (-1 != (len = is.read(bufer)))
                    {
                        fos.write(bufer, 0, len);
                    }
                    fos.close();
                }
            }
        }
        catch (ZipException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(null != is)
                {
                    is.close();
                }
                if(null != fos)
                {
                    fos.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {

            //取出文件夹中的文件（或子文件夹）
            File[] fileList = sourceFile.listFiles();

            if (fileList.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                System.out.println(base + "/");
                out.putNextEntry(new ZipEntry(base + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (int i = 0; i < fileList.length; i++) {
                    compress(out, bos, fileList[i], base==null?(fileList[i].getName()):(base + "/" + fileList[i].getName()));
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);

//            int tag;
            System.out.println(base);
            //将源文件写入到zip文件中
//            while ((tag = bis.read()) != -1) {
//                bos.write(tag);
//            }
            int len;
            byte [] buffer = new byte[1024*8];
            while((len = bis.read(buffer,0,buffer.length)) > 0){
                bos.write(buffer,0,len);
            }
            bis.close();
            fos.close();
            bos.flush();

        }
    }
}

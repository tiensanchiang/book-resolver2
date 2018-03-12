package com.github.tiensanqiang.book.core;


import com.github.tiensanqiang.book.util.StringUtil;
import com.github.tiensanqiang.book.util.ZipUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class BookHandler {

    private String path ;
    private String fileName;
    private String dataPath;
    private String resultPath;

    public BookHandler(String path) {
        this.path = path;
        File file = new File(path);
        fileName = file.getName();
        dataPath = getPath("data");
        resultPath = getPath("result") ;

    }

    public String read() throws Exception {


        File root = new File(dataPath);
        if (root.exists()) {
            FileUtils.deleteDirectory(root);
        } else {
            root.mkdirs();
        }

        ZipUtil.unzip(path, dataPath);

        return dataPath;

    }

    public void create() throws Exception {
        String resultFile = resultPath + fileName;
        File file = new File(resultFile);
        if(file.exists())
            file.delete();
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        file.createNewFile();

        ZipUtil.zip(resultFile,dataPath);
    }


    private String getPath(String middle){
        String userDir = System.getProperty("user.dir");
        return StringUtil.paths(userDir, middle, fileName,"");
    }

}

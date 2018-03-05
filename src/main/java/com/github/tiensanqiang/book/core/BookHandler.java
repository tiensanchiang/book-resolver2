package com.github.tiensanqiang.book.core;


import com.github.tiensanqiang.book.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;

public class BookHandler {

    private String path;

    public BookHandler(String path) {
        this.path = path;
    }

    public String read() throws Exception {
        File file = new File(path);
        String userDir = System.getProperty("user.dir");
        String rootDir = StringUtils.join(new String[]{userDir, File.separator,
                "data", File.separator, file.getName(), File.separator});

        File root = new File(rootDir);
        if (root.exists()) {
            FileUtils.deleteDirectory(root);
        } else {
            root.mkdirs();
        }

        ZipUtil.unzip(path, rootDir);

        return rootDir;

    }

    public void create(){

    }

}

package com.github.tiensanqiang.book;

import com.github.tiensanqiang.book.core.*;
import com.github.tiensanqiang.book.util.StringUtil;

import java.io.File;

public class BookResolverEntry {


    public static void main(String[] args) throws Exception {

//        String s = "<span class=\"math-super\"><a id=\"noteBack_1\" href=\"part0004_split_002.html#note_1\">[1]</a></span>";
//        System.out.println(s.matches("<span\\s+(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*<a\\s*(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*[\\[【［（(]?\\s*\\d+\\s*[）)\\]］】]?\\s*</a\\s*>\\s*</span\\s*>"));

//        if (args == null || args.length < 1) {
//            System.err.println("Usage: br  %Epub file name%");
//            return;
//        }

        if(args.length<1) {
            System.err.println("无效参数！");
        }if(args.length == 1){
            process(args[0]);
        }else{
            String path = args[0];
            for(int i=1; i<args.length;i++){
                process(StringUtil.join(File.separatorChar, path,args[i]));
            }
        }

    }

    public static void process(String name) throws Exception{


        System.out.println("\n=================================开始处理文件=================================");
        System.out.println("书籍名称：【" + new File(name).getName() + "】");

        BookHandler reader = new BookHandler(name);
        String dir = reader.read();

        BookResolver resolver = new BookResolver(dir);
        BookDescriptor descriptor = resolver.resolve();

        BookConverter converter = new BookConverter();
        converter.convert(descriptor);
        DocumentPool instance = DocumentPool.instance();

        instance.serialize();

        reader.create(descriptor);
        instance.clear();
        System.out.println("=================================结束处理文件=================================\n");
    }

}

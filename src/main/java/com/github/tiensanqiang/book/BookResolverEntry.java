package com.github.tiensanqiang.book;

import com.github.tiensanqiang.book.core.*;

public class BookResolverEntry {


    public static void main(String[] args) throws Exception {

        String s = "<span class=\"no-style-override7\"><a id=\"link_35\" href=\"part0013.html#link_34\" class=\"calibre6\"><sup class=\"calibre5\">[3]</sup></a><br class=\"calibre3\"/></span>";
        System.out.println(s.matches("<span\\s+(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*<a\\s*(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*<sup\\s+(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*[\\[【［（(]?\\s*\\d+\\s*[）)\\]］】]?\\s*</sup></a\\s*>\\s*(<br class=\"\\w+\"/>)?</span\\s*>"));

        if (args == null || args.length < 1) {
            System.err.println("Usage: br  %Epub file name%");
            return;
        }

        BookHandler reader = new BookHandler(args[0]);
        String dir = reader.read();

        BookResolver resolver = new BookResolver(dir);
        BookDescriptor descriptor = resolver.resolve();

        BookConverter converter = new BookConverter();
        converter.convert(descriptor);
        DocumentPool instance = DocumentPool.instance();

//        System.out.println(instance.get("part0011.xhtml"));
       instance.serialize();

       reader.create();
    }

}

package com.github.tiensanqiang.book;

import com.github.tiensanqiang.book.core.*;

public class BookResolverEntry {


    public static void main(String[] args) throws Exception {

//        String s = "<a id=\"j2\" href=\"part0054.html#j2\"><sup class=\"calibre16\">【2】</sup></a>";
//        System.out.println(s.matches("<a\\s+(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*<sup\\s*>\\s*[\\[【［（(]?\\s*\\d+\\s*[）)\\]］】]?\\s*</sup\\s*>\\s*</a\\s*>"));

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

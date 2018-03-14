package com.github.tiensanqiang.book;

import com.github.tiensanqiang.book.core.*;

public class BookResolverEntry {


    public static void main(String[] args) throws Exception {

        String s = "<sup class=\"suptext\"><a id=\"fhzs3\" href=\"part0250.xhtml#zhushi3\">〔3〕</a></sup>";
        System.out.println(s.matches("<sup\\s*(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*<a\\s*(\\w+\\s*=\\s*\\\"(.*?)\\\")*\\s*>\\s*[\\[【［（〔(]?\\s*\\d+\\s*[）)\\]］】]?\\s*</a\\s*>\\s*</sup\\s*>"));

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

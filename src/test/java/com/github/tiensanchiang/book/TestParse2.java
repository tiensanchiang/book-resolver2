package com.github.tiensanchiang.book;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestParse2 {

    @Test
    public void test() throws Exception {

        File dir = new File("D:/Temporary/html/");
        File[] htmls = dir.listFiles();

        for (File file : htmls) {
            String html = IOUtils.toString(new FileInputStream(file), "UTF-8");
            Document doc = Jsoup.parse(html);
            Elements sups = doc.select("sup");
            for(Element sup : sups){
                Element a = sup.parent().parent().nextElementSibling();
                if(a != null && a.tagName().equals("a") && a.attr("id") != null){
                    a.remove();
                    sup.before(a);
                }
            }


            File res = new File("D:/Temporary/result/" + file.getName());
            IOUtils.write(doc.toString(), new FileOutputStream(res), "UTF-8");
        }
    }
}

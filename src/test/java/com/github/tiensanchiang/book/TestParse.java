package com.github.tiensanchiang.book;

import com.github.tiensanqiang.book.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.util.Iterator;

public class TestParse {

    @Test
    public void test() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("html/part0119.xhtml");

        File dir = new File("D:/Temporary/html/");
        File[] htmls = dir.listFiles();

        for (File file : htmls) {
            String html = IOUtils.toString(new FileInputStream(file), "UTF-8");
            Document doc = Jsoup.parse(html);
            Elements select = doc.select("img.yiwen-footnote");

            if (select.size() > 0) {
                for (Element e : select) {
                    e.before(createNote(doc, e));
                    createFoot(doc, e);
                }

                Iterator<Element> iterator = select.iterator();
                while (iterator.hasNext()) {
                    iterator.next().remove();
                }
                addCss(doc);
            }

            File res = new File("D:/Temporary/result/" + file.getName());
            IOUtils.write(doc.toString(), new FileOutputStream(res), "UTF-8");
        }
    }

    private Element createNote(Document document, Element imgEle) {
        Element link = document.createElement("a");
        Element img = document.createElement("img");
        link.appendChild(img);

        link.attr("class", "duokan-footnote");
        link.attr("href", "#foot-" + imgEle.id());
        link.attr("id", imgEle.id());

        img.attr("alt", "注释");
        img.attr("class", "duokan-footnote");
        img.attr("src", "../Images/note.png");

        return link;
    }

    private Element createFoot(Document document, Element imgEle) {
        Element ol;
        if ((ol = document.selectFirst("ol.duokan-footnote-content")) == null) {
            ol = document.createElement("ol");
            ol.attr("class", "duokan-footnote-content");

            Element body = document.selectFirst("body");
            body.appendChild(ol);
        }

        Element li = document.createElement("li");
        Element p = document.createElement("p");
        Element a = document.createElement("a");
        p.appendChild(a);
        li.appendChild(p);

        li.attr("class", "duokan-footnote-item");
        li.attr("id", "foot-" + imgEle.id());

        p.attr("class", "footnote-text");

        a.attr("class", "duokan-footnote-link");
        a.attr("href", "#" + imgEle.id());
        a.text(imgEle.attr("alt"));

        ol.appendChild(li);
        return ol;
    }

    public void addCss(Document document) {
        Element link = document.createElement("link");

        link.attr("href", "../Styles/note.css");
        link.attr("rel", "stylesheet");
        link.attr("type", "text/css");
        document.selectFirst("head").appendChild(link);
    }
}

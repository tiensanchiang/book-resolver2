package com.github.tiensanqiang.book.core;

import com.github.tiensanqiang.book.config.FormatDom;
import com.github.tiensanqiang.book.config.IndexFormat;
import com.github.tiensanqiang.book.config.NoteFormat;
import com.github.tiensanqiang.book.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteFinder {

    private IndexFormat index;

    public Note getNote(Document document) throws IOException {

//        if (index == null && !guessDocNoteFormat(document)) {
//            return null;
//        }
        if(!guessDocNoteFormat(document))
            return null;

        Note note = new Note();

        Pattern pattern = index.getPattern();
        Matcher matcher = pattern.matcher(document.toString());
        while (matcher.find()) {
            String group = matcher.group();
            Document fragment = Jsoup.parse(group);
            Elements all = fragment.getAllElements();

            String id = null, href = null;
            for (Element e : all) {
                if (!StringUtil.noe(e.attr("id"))) {
                    id = e.attr("id");
                }
                if (!StringUtil.noe(e.attr("href"))) {
                    href = e.attr("href");
                }
            }

            if (StringUtil.noe(id) || StringUtil.noe(href)){
                System.err.println("标识或链接为空！" + group );
                continue;
            }

            Note.Index item = note.new Index();
            item.setFormat(index);
            item.setId(id);
            item.setHref(href);

            String[] parts = href.split("#");
            if(parts.length<2)
                continue;

            Note.Foot foot = note.new Foot();
            foot.setId(parts[1]);

            Element footElement;
            if (parts[0].trim().length() > 0 && !new File(document.location()).getName().equals(parts[0])) {
                Document doc = Jsoup.parse(new File(StringUtil.paths(new File(document.location()).getParent(), parts[0])), "utf-8");
                footElement = doc.selectFirst("#" + parts[1]);
            } else {
                footElement = document.selectFirst("#" + parts[1]);
            }

            id = null;
            href = null;
            String text = null;
            List<FormatDom> doms = index.getFootNoteFormat().getDoms();
            for (FormatDom dom : doms) {
                Element e = getRelationElement(footElement, dom.getRelation());

                if (!StringUtil.noe(e.attr("id"))) {
                    id = e.attr("id");
                }
                if (!StringUtil.noe(e.attr("href"))) {
                    href = e.attr("href");
                }

                if (dom.isTextual())
                    text = e.text();
            }
            foot.setId(id);
            foot.setHref(href);
            foot.setFormat(index.getFootNoteFormat());
            foot.setText(text);

            item.setFoot(foot);
            note.addIndex(item);
        }

        return note;
    }

    private Element getRelationElement(Element e, String rel) {

        if (StringUtil.noe(rel))
            return e;

        Element result = e;
        String split[] = rel.split("\\.");
        for (String prop : split) {
            result = doRelationElement(result, prop);
        }

        if (result == null)
            System.err.println("节点不存在！");
        return result;

    }

    private Element doRelationElement(Element e, String prop) {
        try {

            if (prop.startsWith("$")) {
                return e.selectFirst(prop.replaceAll("\\$", ""));
            } else {
                Method method = Element.class.getMethod(prop);
                return (Element) method.invoke(e);
            }

        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }
    }

    private boolean guessDocNoteFormat(Document document) throws IOException {
        NoteFormat format = NoteFormat.instance();
        List<IndexFormat> indices = format.getIndices();

        for (IndexFormat fmt : indices) {
            Pattern pattern = Pattern.compile(fmt.getExpression());
            Matcher matcher = pattern.matcher(document.toString());
            if (matcher.find()) {

                String group = matcher.group();
                Document parse = Jsoup.parse(group);

                Elements select = parse.select("[id]");
                if (select.size() == 0 || select.size() > 1) {
                    System.err.println("注释内容不包含或包含多个拥有id属性的元素！" + parse.html());
                    continue;
                }

                if (!matchDoms(select.get(0), fmt.getDoms()))
                    continue;

                Elements element = parse.select("[href]");
                if (element.size() == 0 || element.size() > 1) {
                    System.err.println("注释不包含或包含多个拥有href属性的元素！" + parse.html());
                    continue;
                }

                Document doc = document;
                String href = element.attr("href");
                String[] parts = href.split("#");
                if(parts.length<2)
                    continue;
                if (!StringUtil.noe(parts[0]) && !parts[0].equals(new File(document.location()).getName())) {
                    doc = Jsoup.parse(new File(StringUtil.paths(new File(document.location()).getParent(), parts[0])), "utf-8");
                }

                Element footEle = doc.selectFirst("#" + parts[1]);
                if (!matchDoms(footEle, fmt.getFootNoteFormat().getDoms()))
                    continue;

                index = fmt;
                index.setPattern(pattern);
                System.out.println("发现注释格式#" + fmt.getId() + ",样例：" + fmt.getExample());
                return true;
            }
        }

        return false;
    }

    private boolean matchDoms(Element element, List<FormatDom> doms) {
//        System.out.println(element);

        for (FormatDom dom : doms) {
            String relation = dom.getRelation();
            if (!StringUtil.noe(relation)) {

                Element ele = getRelationElement(element,relation);


                boolean b = true;
                for (String attr : dom.getAttr()) {
                    b = ele.hasAttr(attr);
                    if (!b)
                        break;
                }
                b = b && ele !=null && ele.tagName().equalsIgnoreCase(dom.getTagName());

                if (!b) {
                    return false;
                }
            } else {
                if (!element.tagName().equalsIgnoreCase(dom.getTagName()))
                    return false;
            }

        }


        return true;
    }
}

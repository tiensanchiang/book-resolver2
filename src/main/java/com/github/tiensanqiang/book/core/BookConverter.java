package com.github.tiensanqiang.book.core;

import com.github.tiensanqiang.book.config.FootNoteFormat;
import com.github.tiensanqiang.book.config.FormatDom;
import com.github.tiensanqiang.book.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.print.Doc;
import java.awt.print.Book;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookConverter {

    private NoteFinder finder;

    private DocumentPool pool;

    public BookConverter() {
        finder = new NoteFinder();
        pool = DocumentPool.instance();
    }

    public void convert(BookDescriptor descriptor) throws IOException {

        BookContentPaths paths = descriptor.getPaths();
        OpenPublicationFormat opf = descriptor.getOpf();
        List<ManifestItem> manifest = opf.getManifest();

        for (ManifestItem item : manifest) {
            if (item.isHyperText()) {

                String path = StringUtil.paths(paths.getRootPath(), opf.getPath());
                path = StringUtil.paths(new File(path).getParent(), item.getHref());
                String name = StringUtil.name(path);

                Document document;
                if ((document = pool.get(name)) == null)
                    document = Jsoup.parse(new File(path), "utf-8");
                Note note = finder.getNote(document);

                if (note != null && note.getIndices().size() > 0) {
                    List<Note.Index> indices = note.getIndices();
                    System.out.println("文档" + document.location() + "发现" + indices.size() + "个注释。");

                    if (pool.get(name) == null)
                        pool.add(StringUtil.name(document.location()), document);

                    addImageAndStyles(descriptor, document);

                    for (Note.Index index : indices) {
                        createImageNote(descriptor, document, index);
                        createImageFoot(descriptor, document, index);
                    }
                } else {
                    System.out.println("文档" + document.location() + "不包含注释！");
                }

            }
        }
    }

    public void createImageNote(BookDescriptor descriptor, Document document, Note.Index index) {

        BookContentPaths paths = descriptor.getPaths();

        Element link = document.createElement("a");
        Element img = document.createElement("img");
        link.appendChild(img);

        String href = index.getHref();
        String[] parts = href.split("#");
        String rid = parts[1];
        if (rid.equals(index.getId())) {
            rid = StringUtil.join("_", rid);
            href = StringUtil.join(parts[0], "#", rid);
        }


        link.attr("class", "duokan-footnote");
        link.attr("href", href.substring(href.indexOf('#')));
        link.attr("id", index.getId());

        img.attr("alt", "注释");
        img.attr("class", "duokan-footnote");
        img.attr("src", StringUtil.join(paths.getImageHrefPrefix(), "/note.png"));


        Element e = document.selectFirst("#" + index.getId());
        FormatDom appendableDom = index.getFormat().getAppendableDom();
        Element re = getRelationElement(e, appendableDom.getRelation());
        if (re == null)
            return;
        re.after(link);


        List<FormatDom> removables = index.getFormat().getRemovableDom();
        List<Element> collect = removables.stream().map(f -> getRelationElement(e, f.getRelation())).collect(Collectors.toList());

        collect.forEach(c -> {
            if (c != null) c.remove();
        });

    }

    public void createImageFoot(BookDescriptor descriptor, Document document, Note.Index index) throws IOException {
        String loc = document.location();
        String name = StringUtil.name(document.location());
        String[] parts = index.getHref().split("#");

        Document refDocument = document;
        if (parts[0].trim().length() > 0 && !parts[0].equals(name)) {
            if ((refDocument = pool.get(parts[0])) == null) {
                refDocument = Jsoup.parse(new File(StringUtil.paths(new File(loc).getParent(), parts[0])), "utf-8");
                pool.add(parts[0], refDocument);
            }
        }

        Note.Foot foot = index.getFoot();
        if(foot.getHref() == null)
            return;
        String id = foot.getId();
        Element element = refDocument.selectFirst("#" + id);

        List<Element> images = new ArrayList<>();
        if (element != null) {
            FootNoteFormat format = foot.getFormat();
            List<FormatDom> removables = format.getRemovableDom();
            List<Element> collect = removables.stream().map(f -> getRelationElement(element, f.getRelation())).collect(Collectors.toList());

            collect.forEach(c -> {
                Element img = c.selectFirst("img");
                if (img != null && images.size() == 0)
                    images.add(img.clone());
                c.remove();
            });

            if (index.getId().equals(id)) {
                id = StringUtil.join("_", id);
            }
        }

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
        li.attr("id", id);

        p.attr("class", "footnote-text");

        a.attr("class", "duokan-footnote-link");
        a.attr("href", foot.getHref().indexOf('#')>=0?foot.getHref().substring(foot.getHref().indexOf('#')):foot.getHref());
        a.text(foot.getText());
        if (images.size() > 0) {
            for (Element image : images) {
                a.prependChild(image);
            }
        }

        ol.appendChild(li);

    }

    private Element getRelationElement(Element e, String rel) {
        try {
            if (StringUtil.noe(rel)) {
                return e;
            } else if (rel.startsWith("$")) {
                return e.selectFirst(rel.replaceAll("\\$", ""));
            } else {
                if (e == null)
                    return null;
                Element res = e;
                String parts[] = rel.split("\\.");
                for (String p : parts) {
                    Method method = Element.class.getMethod(p);
                    res = (Element) method.invoke(e);
                }
                return res;
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

    public void addImageAndStyles(BookDescriptor descriptor, Document document) {
        BookContentPaths paths = descriptor.getPaths();
        File imagesPath = new File(paths.getImagePath());
        if (!imagesPath.exists()) {
            imagesPath.mkdirs();
        }

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("note.png");
             OutputStream os = new FileOutputStream(new File(imagesPath.getPath() + File.separator + "note.png"))) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File stylesPath = new File(paths.getStylePath());
        if (!stylesPath.exists()) {
            stylesPath.mkdirs();
        }

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("note.css");
             OutputStream os = new FileOutputStream(new File(stylesPath.getPath() + File.separator + "note.css"))) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element head = document.selectFirst("head");
        Element link = document.createElement("link");

        if(StringUtil.noe(paths.getStyleHrefPrefix())){
            link.attr("href", StringUtil.join("note.css"));
        }else{
            link.attr("href", StringUtil.join(paths.getStyleHrefPrefix(), "/note.css"));
        }
        link.attr("rel", "stylesheet");
        link.attr("type", "text/css");
        head.appendChild(link);
    }
}

package com.github.tiensanqiang.book.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NoteFormat {

    private List<IndexFormat> indices;
    private List<FootNoteFormat> footnotes;


    public static NoteFormat format;

    private NoteFormat() {

    }

    public static NoteFormat instance() {
        if (format == null) {
            synchronized (NoteFormat.class) {
                if (format == null) {
                    format = new NoteFormat();
                    format.init();
                }
            }
        }

        return format;
    }


    private void init() {
        SAXReader reader = new SAXReader();

        Document document = null;
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("formats.cfg.xml")) {
            document = reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        indices = new ArrayList<>();
        footnotes = new ArrayList<>();

        Element root = document.getRootElement();

        List<Element> footnotes = root.element("footnotes").elements("footnote");
        for(Element fn : footnotes){
            FootNoteFormat f = new FootNoteFormat();
            f.setId(fn.attributeValue("id"));
            f.setDoms(getFormatDoms(fn.element("doms")));

            this.footnotes.add(f);
        }

        List<Element> index = root.element("indices").elements("index");
        for(Element idx : index){
            IndexFormat fmt = new IndexFormat();
            fmt.setId(idx.attributeValue("id"));
            fmt.setExample(idx.elementText("example").trim());
            fmt.setExpression(idx.elementText("expression").trim());
            fmt.setSelector(idx.attributeValue("idx"));
            fmt.setDoms(getFormatDoms(idx.element("doms")));
            fmt.setFootNoteFormat(getFootNoteById(idx.attributeValue("footnote")));


            indices.add(fmt);
        }
    }

    private List<FormatDom> getFormatDoms(Element doms){
        List<FormatDom> result = new ArrayList<>();
        List<Element> dom  = doms.elements("dom");
        for(Element d : dom){
            FormatDom fd = new FormatDom();
            fd.setTagName(d.attributeValue("tag-name"));
            fd.setAttr(d.attributeValue("attr"));
            fd.setRelation(d.attributeValue("rel"));
            fd.setRemovable(Boolean.parseBoolean(d.attributeValue("remove")));
            fd.setTextual(Boolean.parseBoolean(d.attributeValue("text")));
            fd.setAppendable(Boolean.parseBoolean(d.attributeValue("append")));
            result.add(fd);
        }

        return result;
    }


    private FootNoteFormat getFootNoteById(String id){
        for(FootNoteFormat f : footnotes){
            if(id.equals(f.getId()))
                return f;
        }
        return null;
    }

    public List<IndexFormat> getIndices() {
        return indices;
    }

    public void setIndices(List<IndexFormat> indices) {
        this.indices = indices;
    }

    public List<FootNoteFormat> getFootnotes() {
        return footnotes;
    }

    public void setFootnotes(List<FootNoteFormat> footnotes) {
        this.footnotes = footnotes;
    }
}

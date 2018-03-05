package com.github.tiensanqiang.book.core;

import com.github.tiensanqiang.book.config.FootNoteFormat;
import com.github.tiensanqiang.book.config.FormatDom;
import com.github.tiensanqiang.book.config.IndexFormat;

import java.util.ArrayList;
import java.util.List;

public class Note {

    private List<Index> indices;

    public Note() {
        indices = new ArrayList<>();
    }

    public List<Index> getIndices() {
        return indices;
    }

    public void setIndices(List<Index> indices) {
        this.indices = indices;
    }

    public void addIndex(Index index){
        indices.add(index);
    }

    public class Index {

        private String id;
        private String href;
        private Foot foot;
        private IndexFormat format;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public Foot getFoot() {
            return foot;
        }

        public void setFoot(Foot foot) {
            this.foot = foot;
        }

        public IndexFormat getFormat() {
            return format;
        }

        public void setFormat(IndexFormat format) {
            this.format = format;
        }
    }

    public class Foot{
        private String id;
        private String href;
        private String text;
        private FootNoteFormat format;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public FootNoteFormat getFormat() {
            return format;
        }

        public void setFormat(FootNoteFormat format) {
            this.format = format;
        }
    }
}

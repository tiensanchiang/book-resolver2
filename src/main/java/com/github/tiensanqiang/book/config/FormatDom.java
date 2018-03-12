package com.github.tiensanqiang.book.config;

public class FormatDom {

    private String tagName;
    private String relation;
    private String [] attr;
    private boolean textual;
    private boolean removable;
    private boolean appendable;


    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String [] getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr==null?new String[0]:attr.split(",");
    }

    public boolean isTextual() {
        return textual;
    }

    public void setTextual(boolean textual) {
        this.textual = textual;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public boolean isAppendable() {
        return appendable;
    }

    public void setAppendable(boolean appendable) {
        this.appendable = appendable;
    }
}

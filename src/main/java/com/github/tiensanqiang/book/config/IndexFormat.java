package com.github.tiensanqiang.book.config;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IndexFormat {
    private String id;
    private String expression;
    private Pattern pattern;
    private String example;
    private FootNoteFormat footNoteFormat;

    private List<FormatDom> doms;


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public List<FormatDom> getDoms() {
        return doms;
    }

    public void setDoms(List<FormatDom> doms) {
        this.doms = doms;
    }

    public FootNoteFormat getFootNoteFormat() {
        return footNoteFormat;
    }

    public void setFootNoteFormat(FootNoteFormat footNoteFormat) {
        this.footNoteFormat = footNoteFormat;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FormatDom getAppendableDom(){
        for (FormatDom d : doms){
            if(d.isAppendable())
                return d;
        }
        throw new RuntimeException("无可追加的dom元素！");
    }

    public List<FormatDom> getRemovableDom(){
        List<FormatDom> result = new ArrayList<>();
        for(FormatDom d : doms){
            if(d.isRemovable())
                result.add(d);
        }

        return result;
    }
}

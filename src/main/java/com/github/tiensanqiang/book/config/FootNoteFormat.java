package com.github.tiensanqiang.book.config;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FootNoteFormat {
    private String id;
    private List<FormatDom> doms;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FormatDom> getDoms() {
        return doms;
    }

    public void setDoms(List<FormatDom> doms) {
        this.doms = doms;
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

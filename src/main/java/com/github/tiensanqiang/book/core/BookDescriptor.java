package com.github.tiensanqiang.book.core;

public class BookDescriptor {


    private String title;

    private BookContentPaths paths;
    private OpenPublicationFormat opf;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookContentPaths getPaths() {
        return paths;
    }

    public void setPaths(BookContentPaths paths) {
        this.paths = paths;
    }

    public OpenPublicationFormat getOpf() {
        return opf;
    }

    public void setOpf(OpenPublicationFormat opf) {
        this.opf = opf;
    }
}

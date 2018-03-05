package com.github.tiensanqiang.book.core;

public class BookContentPaths {

    private String rootPath;
    private String imagePath;
    private String stylePath;
    private String textPath;

    private String imageHrefPrefix;
    private String styleHrefPrefix;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStylePath() {
        return stylePath;
    }

    public void setStylePath(String stylePath) {
        this.stylePath = stylePath;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setTextPath(String textPath) {
        this.textPath = textPath;
    }

    public String getImageHrefPrefix() {
        return imageHrefPrefix;
    }

    public void setImageHrefPrefix(String imageHrefPrefix) {
        this.imageHrefPrefix = imageHrefPrefix;
    }

    public String getStyleHrefPrefix() {
        return styleHrefPrefix;
    }

    public void setStyleHrefPrefix(String styleHrefPrefix) {
        this.styleHrefPrefix = styleHrefPrefix;
    }
}

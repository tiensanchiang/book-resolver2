package com.github.tiensanqiang.book.core;

public class ManifestItem {

    private String id;
    private String href;
    private String mediaType;

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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isHyperText(){
        return mediaType != null && mediaType.equals("application/xhtml+xml");
    }

    public boolean isStyleSheet(){
        return mediaType != null && mediaType.equals("text/css");
    }

    public boolean isImage(){
        return mediaType != null && (mediaType.equals("image/png") || mediaType.equals("image/jpeg"));
    }
}

package com.github.tiensanqiang.book.core;

import java.util.List;

public class OpenPublicationFormat {

    private String path;

    private List<ManifestItem> manifest;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ManifestItem> getManifest() {
        return manifest;
    }

    public void setManifest(List<ManifestItem> manifest) {
        this.manifest = manifest;
    }
}

package com.github.tiensanqiang.book.core;

import com.github.tiensanqiang.book.util.Constants;
import com.github.tiensanqiang.book.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookResolver{

    private String rootPath;

    public BookResolver(String rootPath) {
        this.rootPath = rootPath;
    }

    public BookDescriptor resolve() throws DocumentException {

        BookDescriptor descriptor = new BookDescriptor();

        String contentFile = getOpenPublicationFormatFile();
        OpenPublicationFormat opf = getOpenPublicationFormat(
                new File(StringUtil.join(File.separatorChar,rootPath,contentFile)));
        opf.setPath(contentFile);
        descriptor.setOpf(opf);

        BookContentPaths contentPaths = getContentPaths(opf);
        descriptor.setPaths(contentPaths);

        return descriptor;
    }

    /**
     * 通过META-INF/container.xml文件或去opf文件路径
     * @return
     * @throws DocumentException
     */
    private String getOpenPublicationFormatFile() throws DocumentException {
        String path = StringUtil.join(File.separatorChar, rootPath, Constants.DIR_METAINFO, Constants.FILE_CONTAINER);
        File file = new File(path);
        if (!file.exists())
            throw new RuntimeException("容器元数据文件不存在！");
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        Element rootfiles = root.element("rootfiles");
        List<Element> rootfile = rootfiles.elements("rootfile");
        String fullPath = rootfile.get(0).attributeValue("full-path");

        return fullPath;
    }

    private OpenPublicationFormat getOpenPublicationFormat(File contentFile) throws DocumentException {

        OpenPublicationFormat opf = new OpenPublicationFormat();
        SAXReader reader = new SAXReader();
        Document document = reader.read(contentFile);
        Element root = document.getRootElement();

        BookDescriptor metaInfo = new BookDescriptor();
        Element metadata = root.element("metadata");
        metaInfo.setTitle(getElementText(metadata.element("title")));

        List<ManifestItem> manifestItems = new ArrayList<>();
        Element manifest = root.element("manifest");
        List<Element> items = manifest.elements("item");
        for (Element e : items) {
            ManifestItem item = new ManifestItem();
            item.setId(getElementAttr(e, "id"));
            item.setHref(getElementAttr(e, "href"));
            item.setMediaType(getElementAttr(e, "media-type"));
            manifestItems.add(item);
        }
        opf.setManifest(manifestItems);

        return opf;
    }

    private BookContentPaths getContentPaths(OpenPublicationFormat opf){

        BookContentPaths paths = new BookContentPaths();
        List<ManifestItem> manifest = opf.getManifest();

        String prefix = "";
        String[] parts = opf.getPath().split("[/\\\\]");
        if(parts.length>1)
            prefix = parts[0];

        //paths.setContentPath(StringUtil.paths(rootPath,prefix));

        ManifestItem imageItem = getFirstManifestItem(manifest,"image/.*");
        if(imageItem == null)
            throw new RuntimeException("查询不到图片条目！");
        String href = imageItem.getHref();
        String[] split = href.split("[/\\\\]");
        if(split.length>1){
            paths.setImagePath(StringUtil.join(File.separatorChar,rootPath,prefix,split[0]));
        }else{
            paths.setImagePath(StringUtil.join(File.separatorChar,rootPath,prefix));
        }

        ManifestItem textItem = getFirstManifestItem(manifest,"application/xhtml\\+xml");
        if(textItem == null)
            throw new RuntimeException("查询不到文本条目！");
        href = textItem.getHref();
        split = href.split("[/\\\\]");
        if (split.length > 1) {
            paths.setTextPath(StringUtil.join(File.separatorChar,rootPath,prefix,split[0]));
        }else{
            paths.setTextPath(StringUtil.join(File.separatorChar,rootPath,prefix));
        }

        ManifestItem styleItem = getFirstManifestItem(manifest,"text/css");
        if(styleItem== null)
            throw new RuntimeException("查询不到文本条目！");
        href = styleItem.getHref();
        split = href.split("[/\\\\]");
        if (split.length > 1) {
            paths.setStylePath(StringUtil.join(File.separatorChar,rootPath,prefix,split[0]));
        }else{
            paths.setStylePath(StringUtil.join(File.separatorChar,rootPath,prefix));
        }

        Path base = Paths.get(paths.getTextPath());
        Path text = Paths.get(paths.getImagePath());
        Path relative = base.relativize(text);
        paths.setImageHrefPrefix(relative.toString().replaceAll("[\\\\]","/"));

        Path style = Paths.get(paths.getStylePath());
        relative = base.relativize(style);
        paths.setStyleHrefPrefix(relative.toString().replaceAll("[\\\\]","/"));

        paths.setRootPath(rootPath);
        return paths;
    }

    private String getElementText(Element e) {
        if (e == null)
            return null;
        return e.getText();
    }

    private String getElementAttr(Element e, String attr) {
        if (e == null)
            return null;
        return e.attributeValue(attr);
    }

    private ManifestItem getFirstManifestItem(List<ManifestItem> items,String mediaTypeRegex){

        for(int i= items.size()-1;i>=0;i--){
            ManifestItem item = items.get(i);
            String mediaType = item.getMediaType();
            if(!item.getId().contains("cover") &&  mediaType.matches(mediaTypeRegex))
                return item;
        }

        return null;
    }
}

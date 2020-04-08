package com.github.tiensanqiang.book.core;


import com.github.tiensanqiang.book.util.StringUtil;
import com.github.tiensanqiang.book.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

public class BookHandler {

    private String path ;
    private String fileName;
    private String dataPath;
    private String resultPath;

    public BookHandler(String path) {
        this.path = path;
        File file = new File(path);
        fileName = file.getName();
        dataPath = getPath("data");
        resultPath = getPath("result") ;

    }

    public String read() throws Exception {


        File root = new File(dataPath);
        if (root.exists()) {
            FileUtils.deleteDirectory(root);
        } else {
            root.mkdirs();
        }

        ZipUtil.unzip(path, dataPath);

        return dataPath;

    }

    public void create(BookDescriptor descriptor) throws Exception {

        updateOpenDocumentFile(descriptor);

        String resultFile = resultPath + fileName;
        File file = new File(resultFile);
        if(file.exists())
            file.delete();
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        file.createNewFile();

        ZipUtil.zip(resultFile,dataPath);
    }


    private void updateOpenDocumentFile(BookDescriptor descriptor) throws DocumentException, IOException {

        BookContentPaths paths = descriptor.getPaths();
        String opf = paths.getRootPath() + descriptor.getOpf().getPath();
        String imgHref ;
        if(StringUtil.noe(paths.getImageOpfHrefPrefix())){
            imgHref = "note.png";
        }else{
            imgHref = StringUtil.join(paths.getImageOpfHrefPrefix(), "/note.png");
        }

        String cssHref ;
        if(StringUtil.noe(paths.getStyleOpfHrefPrefix())){
            cssHref = "note.css";
        }else{
            cssHref = StringUtil.join(paths.getStyleOpfHrefPrefix(), "/note.css");
        }
        String imgId = StringUtil.uuid();
        String cssId = StringUtil.uuid();

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(opf));
        Element root = document.getRootElement();
        Element manifest = root.element("manifest");

        Element image = DocumentHelper.createElement(QName.get("item",manifest.getNamespace()));
        image.addAttribute("id",imgId);
        image.addAttribute("href", imgHref);
        image.addAttribute("media-type", "image/png");
        manifest.add(image);

        Element css = DocumentHelper.createElement(QName.get("item",manifest.getNamespace()));
        css.addAttribute("id", cssId);
        css.addAttribute("href", cssHref);
        css.addAttribute("media-type","text/css");
        manifest.add(css);

        Element spine = root.element("spine");
        Element itemref = DocumentHelper.createElement(QName.get("itemref", spine.getNamespace()));
        itemref.addAttribute("idref", imgId);
        spine.add(itemref);
        itemref =  DocumentHelper.createElement(QName.get("itemref", spine.getNamespace()));
        itemref.addAttribute("idref", cssId);
        spine.add(itemref);

        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(opf), format);
        xmlWriter.write(document);
        xmlWriter.close();
    }

    private String getPath(String middle){
        String userDir = System.getProperty("user.dir");
        return StringUtil.paths(userDir, middle, fileName,"");
    }

}

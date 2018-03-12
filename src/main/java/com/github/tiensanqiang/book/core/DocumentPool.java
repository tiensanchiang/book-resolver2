package com.github.tiensanqiang.book.core;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentPool implements Serializable{

    private Map<String,Document> map;

    private static DocumentPool pool;

    private DocumentPool() {
        map = new HashMap<>();
    }

    public static DocumentPool instance(){
        if(pool == null){
            synchronized (DocumentPool.class){
                if(pool == null){
                    pool = new DocumentPool();
                }
            }
        }

        return pool;
    }


    public void add(String id,Document document){
        if(!map.containsKey(id)){
            map.put(id,document);
        }
    }

    public Document get(String id){
        if(map.containsKey(id))
            return map.get(id);
        else
            return null;
    }

    public void serialize(){
        Set<String> keys = map.keySet();

        for(String key : keys){
            Document document = map.get(key);
            try(OutputStream os = new FileOutputStream(new File(document.location()))){
                IOUtils.write(document.toString(),os,"UTF-8");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}

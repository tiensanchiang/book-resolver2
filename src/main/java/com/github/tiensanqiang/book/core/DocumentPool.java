package com.github.tiensanqiang.book.core;

import org.jsoup.nodes.Document;

import java.io.Serializable;
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

        }
    }


}

package com.github.tiensanchiang.book;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJson {

    @Test
    public void testParseJson() throws Exception{
        String str = IOUtils.toString(TestJson.class.getClassLoader().getResource("test.json"));
        Gson gson = new Gson();
        List list = gson.fromJson(str, ArrayList.class);
        for(Object o : list){
            Map m = (Map)o;
            System.out.println(m.get("link"));
        }
    }
}

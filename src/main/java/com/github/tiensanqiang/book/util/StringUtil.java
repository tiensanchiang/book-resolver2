package com.github.tiensanqiang.book.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.UUID;

/**
 * 字符串常用方法
 * @author tiansanqiang
 * @since 3.0.0.0
 */
public final class StringUtil {

    /**
     * 获取uuid
     * @return
     */
    public static String uuid(){
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "").toUpperCase();
    }

    /**
     * 判断字符串为null或空
     * @param str
     * @return
     */
    public static boolean noe(String str){
        return StringUtils.isBlank(str);
    }

    public static String join(String ... str){
        return StringUtils.join(str);
    }

    public static String join(char sep,String ... str){
        return StringUtils.join(str,sep);
    }

    public static String paths(String ... str){
        return join(File.separatorChar,str);
    }

    public static String name(String path){
        return new File(path).getName();
    }

}

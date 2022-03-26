package com.fhz.utils;

/**
 * 时间:11/8/21
 *
 * @author msfeng
 * 简述: TODO
 */
public class StringUtils {
    public static boolean isEmpty(String s){
        if(s.trim().equals("") && s == null){
            return true;
        }
        return false;
    }
}

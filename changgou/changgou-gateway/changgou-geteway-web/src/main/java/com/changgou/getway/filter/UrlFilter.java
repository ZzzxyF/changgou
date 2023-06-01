package com.changgou.getway.filter;


public class UrlFilter {
    private static String[] passUrl={"",""};

    public static boolean hasAuthoriza(String url){
        for(String urlOne:passUrl){
            if(url.equals(urlOne)){
                return true;
            }
        }
        return false;
    }
}

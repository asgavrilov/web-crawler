package com.peer39.service;

public interface WebpageDataStorage {

    String addWebpageHtml(String url, String html);
    String getWebpageHtml(String url);
//    Webpage addWebpage(Webpage webpage);
//    Webpage getWebpage(String url);

}
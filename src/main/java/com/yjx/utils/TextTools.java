package com.yjx.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TextTools {
    @Autowired
    private HttpUtils httpUtils;

    //从text中获取所有的img标签的src属性，并存入localUrls中
    public static List<String> getLocalUrlsFromText(String text){
        Document doc = Jsoup.parse(text);
        Elements imgElements = doc.select("img");
        List<String> localUrls = new ArrayList<>();
        for (Element img : imgElements) {
            img.removeAttr("alt");  // 删除alt属性
            img.removeAttr("style");  // 删除style属性
            String src = img.attr("src");// 获取 img 元素的 src 属性值
            localUrls.add(src);
        }
        return localUrls;
    }
}

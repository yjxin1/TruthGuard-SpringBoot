package com.yjx.service;

import com.yjx.pojo.Result;
import org.springframework.stereotype.Service;

@Service
public interface CrawlerService {
    //根据UrlId爬取较真文章
    boolean crawlJZ(String urlId);

    boolean crawlZH(String urlId);
}

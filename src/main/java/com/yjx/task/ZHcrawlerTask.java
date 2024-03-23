package com.yjx.task;

import com.yjx.service.CrawlerService;
import com.yjx.utils.BloomFilterUtils;
import com.yjx.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ZHcrawlerTask {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private BloomFilterUtils bloomFilterUtils;

//    @Scheduled(cron = "0 0 0 * * ?")  //每天凌晨爬取一次
//    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 每隔24小时执行一次
    @Scheduled(fixedDelay = 24*60*60 * 1000)  //5s一次，获取10条文章链接（id）
//    @Scheduled(fixedDelay = 3600* 1000)  //5s一次，获取5条文章链接（id）
    public void jzCrawler() throws Exception {
        String url = "http://www.piyao.org.cn/";
        String html = httpUtils.doGetHtml(url);
        Document document = Jsoup.parse(html);
        Element content = document.getElementById("focusListNews");
        if (content != null) {
            Element ulElement = content.getElementsByTag("ul").get(1);
            Elements liElements = ulElement.select("li");
            for(Element liElement:liElements){
                Element aElement = liElement.select("a").first();
                if(aElement!=null){
                    String href = aElement.attr("href");
                    if (!bloomFilterUtils.duplicateCheck(href)) {  //如果请求网址不重复
                        log.info("爬取id:" + href);
                        boolean is = crawlerService.crawlZH(href);
                        if (is) {  //爬取成功
                            bloomFilterUtils.urlPut(href);
                        }
                    } else {
                        log.info("id重复了：" + href);
                    }
                }
            }
            bloomFilterUtils.persistBloom(); //数据持久化
        }else{
            log.info("爬取内容为空");
            return;
        }
        return;
    }
}



package com.yjx.task;

import com.yjx.mapper.HotMapper;
import com.yjx.pojo.Hot;
import com.yjx.service.CrawlerService;
import com.yjx.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class WeiboHotTask {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private HotMapper hotMapper;

    @Scheduled(fixedDelay = 3600 * 1000)  //1小时一次
    public void weiboHot(){
        log.info("爬取微博热搜榜单");
        String html = httpUtils.doGetHtml("https://s.weibo.com/top/summary?cate=realtimehot");
        Document document = Jsoup.parse(html);
        Element tbody = document.getElementsByTag("tbody").first();
//        System.out.println("@@@"+tbody);
        Elements tr = tbody.getElementsByTag("tr");
//        System.out.println(tr);
        for(Element item:tr){
            String rankText = item.getElementsByClass("td-01").text();
            Short rank = null;
            try {
                rank = Short.parseShort(rankText);
            } catch (NumberFormatException e) {
                System.out.println("转化不了" + rankText);
            }
            String topic = item.select("td.td-02>a").first().text();
            String link = "https://s.weibo.com/"+item.select("td.td-02>a").first().attr("href");
            Elements labelElements = item.select("td.td-02>span");
            String label="";
            if(labelElements.first() != null){
                label = labelElements.first().text();
            }
            //封装Hot
            Hot hot=new Hot();
            hot.setRank(rank);
            hot.setTopic(topic);
            hot.setLabel(label);
            hot.setLink(link);
            hot.setTimestamp(LocalDateTime.now());
//            System.out.println(hot.toString());
//            System.out.println("------------");
            hotMapper.insert(hot);
        }

    }
}

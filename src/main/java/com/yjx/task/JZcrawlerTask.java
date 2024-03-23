package com.yjx.task;


import com.yjx.service.CrawlerService;
import com.yjx.utils.BloomFilterUtils;
import com.yjx.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class JZcrawlerTask {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    private String jzToken = "U2FsdGVkX19xPFH5an%252F8CD9oA1FQywS4%252Btru9fVCty5OMGLViQ%252Fz%252BQTR6S7YcBnD"; //token暂时是手动获取的
    private Integer page = 0;
    private String locale = "zh-CN";
    //较真爬虫，由于较真网站已经不更新内容了，所以注释了定时器，不再爬取。
//    @Scheduled(fixedDelay = 5 * 1000)  //5s一次，获取10条文章链接（id）
    public void jzCrawler() throws Exception {
        //循环遍历每一个page，一个page可以获取10个文章id
        this.page++;
        String url = "https://vp.fact.qq.com/api/article/list" + "?page=" + page + "&locale=" + locale + "&token=" + jzToken;
        String html = httpUtils.doGetHtml(url);
        JSONObject jsData = new JSONObject(html);
        if(jsData.getInt("code")==-1){
            System.out.println("token验证失败");
            return;
        }
        JSONArray listArray = jsData.getJSONObject("data").getJSONArray("list");// 获取 data 下的 list 数组
        if (listArray == null) {
            log.info("较真文章没有了");
        }
        // 遍历 list 数组中的每个对象
        for (int o = 0; o < listArray.length(); o++) {  //爬取每个id
            JSONObject item = listArray.getJSONObject(o);
            // 获取每个对象的 id 属性并打印
            String id = item.getString("id");
            if (!bloomFilterUtils.duplicateCheck(id)) {  //如果id不重复
                log.info("爬取id:" + id);
                boolean is = crawlerService.crawlJZ(id);
                if(is){  //爬取成功
                    bloomFilterUtils.urlPut(id);
                }
            } else {
                log.info("id重复了：" + id);
            }
        }
        bloomFilterUtils.persistBloom(); //数据持久化
        return;
    }
}

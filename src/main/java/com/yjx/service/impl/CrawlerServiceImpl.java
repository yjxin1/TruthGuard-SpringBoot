package com.yjx.service.impl;

import com.yjx.pojo.Article;
import com.yjx.service.ArticleService;
import com.yjx.service.CrawlerService;
import com.yjx.utils.HttpUtils;
import com.yjx.utils.ValueProperties;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CrawlerServiceImpl implements CrawlerService {
    @Autowired
    private ValueProperties valueProperties;
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private ArticleService articleService;

    @Override  //根据UrlId爬取较真文章
    public boolean crawlJZ(String urlId) {
        //分析：较真平台的网页数据虽然可以在html标签里获得，但是发现大部分的数据都是在script标签里存储，数据格式为json格式。下面抓取的是script的数据.
        String baseUrl = "https://vp.fact.qq.com/article?id=";
        String crawlUrl = baseUrl + urlId;
        log.info("正在爬取文章:{}", crawlUrl);
        String html = httpUtils.doGetHtml(crawlUrl);
        if (html == null || html.equals("")) return false;
        else {
            Document document = Jsoup.parse(html);
            Element articleContent = document.select("script#__NEXT_DATA__").first();  //包裹整个文章的数据
            JSONObject jsData = new JSONObject(articleContent.html());  //得到json对象
            JSONObject initialState = jsData.getJSONObject("props").getJSONObject("pageProps").getJSONObject("initialState");
            String title = initialState.getString("title"); //标题
            String rumorText = initialState.getString("rumor");  //流传说法
            Element summary_element = document.select("div.article_verificationP__jA8qd").first();//查证要点的标签容器
            //对查证要点summary_element的div容器进行操作， 移除 div 标签及其子标签的所有属性
            summary_element.removeAttr("class"); // 移除 div 标签的 class 属性
            summary_element.getElementsByTag("p").removeAttr("class"); // 移除 p 标签的 class 属性
            summary_element.getElementsByTag("span").removeAttr("class"); // 移除 span 标签的 class 属性
            String summary = summary_element.toString();//查证要点
//        summary = summary.replaceAll("\\s+", ""); // 使用正则表达式去除多余空格
            String publisher = initialState.getString("author");//发布者
            String dateString = initialState.getString("date");
            LocalDate publishTime = LocalDate.parse(dateString);//发布时间,年月日
            String content = initialState.getString("content");// 辟谣原文，原文带有大量的unicode编码字符串，
            Document doc = Jsoup.parse(content);
            Element bodyElement = doc.body();
            Element debunkText = new Element("div");
            debunkText.html(bodyElement.html());
            bodyElement.replaceWith(debunkText);
//        debunkText = debunkText.replaceAll("\\s+", ""); // 使用正则表达式去除多余空格
            Short crawlSource = 3;//抓取来源为较真
            String markstyle = initialState.getString("markstyle");  //markstyle分别为true,fake,doubt鉴定级别，10真，0假，5半真半假
            Short verificationLevel = null;
            switch (markstyle) {
                case "true":
                    verificationLevel = 10; // 真，对应鉴定级别为10
                    break;
                case "fake":
                    verificationLevel = 0; // 假，对应鉴定级别为0
                    break;
                case "doubt":
                    verificationLevel = 5; // 半真半假，对应鉴定级别为5
                    break;
                default:
                    verificationLevel = 11; // 异常值
                    break;
            }
            //处理debunkText，将里里面的图片url替换为本地的url,并保存图片
            Elements imgElements = debunkText.select("img");
            String localUrl = "";
            List<String> localUrls = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            for (Element img : imgElements) {
                img.removeAttr("alt");  // 删除alt属性
                img.removeAttr("style");  // 删除style属性
                String src = img.attr("src");// 获取 img 元素的 src 属性值
                // 如果 src 字符串不以 "https:" 开头，则添加前缀
                if (!src.startsWith("https:") && !src.startsWith("http:")) {
                    src = "https:" + src;
                }
                localUrl = "/image?imgName=" + httpUtils.doGetImage(src, valueProperties.getImagePath());  //保存图片并返回重命名的图片名字
                localUrls.add(localUrl);
                urls.add(src);
                img.attr("src", localUrl);// 将 src 属性值替换为自己的图片链接
            }

            //封装article
            Article article = new Article();
            article.setCrawlUrl(crawlUrl);
            article.setTitle(title);
            article.setRumorText(rumorText);
            article.setSummary(summary);
            article.setPublisher(publisher);
            article.setPublishTime(publishTime);
            article.setDebunkText(debunkText.toString());
            article.setCrawlSource(crawlSource);
            article.setVerificationLevel(verificationLevel);

            //保存文章，并返回文章id
            Integer articleId = articleService.saveArticle(article);

            //存入图片url到数据库
            articleService.saveImage(urls, localUrls, articleId);

            //存入标签
            List<String> tags = new ArrayList<>();
            JSONArray tagArray = initialState.getJSONArray("tag");
            for (int i = 0; i < tagArray.length(); i++) {
                articleService.saveTag(tagArray.getString(i), articleId);
            }
            log.info("抓取结束");

            return true;
        }
    }

    @Override
    public boolean crawlZH(String urlId) {
        String crawlUrl = "http://www.piyao.org.cn/" + urlId;  //这里
        String articleHtml = httpUtils.doGetHtml(crawlUrl);
        Document document1 = Jsoup.parse(articleHtml);
        Element con_tit = document1.select("div.con_nr>div.con_tit").first();
        Element debunkText = document1.select("div.con_nr>div.con_txt>#detailContent").first();
        log.info("debunkText" + debunkText);
        String title = con_tit.getElementsByTag("h2").first().text();  //标题
        String dateString = con_tit.select("p span").first().text().substring(3).trim(); // 删除前三个字符，并去除前导空白符
        LocalDate publishTime = LocalDate.parse(dateString);
        con_tit.select("p span").remove();
        String publisher = con_tit.getElementsByTag("p").text().substring(3).trim();  //发布者
        //处理文本中的图片，下载图片和替换本地路径
        Elements imgElements = debunkText.select("img");
        String localUrl = "";
        List<String> localUrls = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (Element img : imgElements) {
            img.removeAttr("data-material-id");
            img.removeAttr("data-name");
            String src = img.attr("src");
            if (!src.startsWith("https:") && !src.startsWith("http:")) {
                src = crawlUrl.substring(0, crawlUrl.length() - 6) + src;  //添加前缀
            }
            localUrl = "/image?imgName=" + httpUtils.doGetImage(src, valueProperties.getImagePath());  //保存图片并返回重命名的图片名字
            localUrls.add(localUrl);
            urls.add(src);
            img.attr("src", localUrl);// 将 src 属性值替换为自己的图片链接
        }
        log.info(urls.toString());
        log.info(localUrls.toString());
        log.info(debunkText.toString());
        Short crawlSource = 2;//抓取来源为中互

        //封装article
        Article article = new Article();
        article.setCrawlUrl(crawlUrl);
        article.setTitle(title);
        article.setPublisher(publisher);
        article.setPublishTime(publishTime);
        article.setDebunkText(debunkText.toString());
        article.setCrawlSource(crawlSource);

        //保存文章，并返回文章id
        Integer articleId = articleService.saveArticle(article);
        //存入图片url到数据库
        articleService.saveImage(urls, localUrls, articleId);
        return true;
    }
}

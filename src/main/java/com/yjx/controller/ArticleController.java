package com.yjx.controller;

import com.yjx.pojo.Article;
import com.yjx.pojo.PageBean;
import com.yjx.pojo.Result;
import com.yjx.pojo.Tag;
import com.yjx.service.ArticleService;
import com.yjx.service.CrawlerService;

import com.yjx.utils.BloomFilterUtils;
import com.yjx.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    //根据ID展示文章（带标签）
    @GetMapping("/{id}")
    public Result showArticleById(@PathVariable Integer id) {
        Article article = articleService.getArticleById(id);
        log.info("根据ID展示文章，id:{}", article.getId());
        List<String> tags = articleService.getTagsByArtilceId(id);
        List<Object> dataList = new ArrayList<>();
        dataList.add(article);
        dataList.add(tags);
        return Result.success(dataList);
    }

    //分页展示and条件查询，根据标题、发布者、发布时间、爬取来源，鉴定级别，分页展示文章标题、发布者、发布时间、爬取来源、鉴定级别、爬取链接
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String title, String publisher, Short verificationLevel, Short crawlSource, LocalDate begin, LocalDate end,Short status) {
        log.info("文章分页查询，参数：{},{},{},{},{},{},{},{},{}", page, pageSize, title, publisher, verificationLevel, crawlSource, begin, end,status);
        PageBean pageBean = articleService.page(page, pageSize, title, publisher, verificationLevel, crawlSource, begin, end,status);
        return Result.success(pageBean);
    }

    //添加文章，不存入标签，service那边会有处理存入图片localUrls到数据库的问题
    @PostMapping
    public Result add(@RequestBody Article article) {
        log.info("添加文章：{}", article);
        //添加文章
        if(article.getTitle()==null||article.getTitle()=="") return Result.error("标题未输入");
        Integer id = articleService.add(article);
        return Result.success();
    }
    //修改文章，只允许修改文章标题、发布状态
    @PutMapping
    public Result update(@RequestBody Article article){
        log.info("修改文章id:{}",article.getId());
        articleService.update(article);
        return Result.success();
    }
    //给文章删除标签
    @DeleteMapping("/tag")
    public Result deleteTagByArticleId(Integer id,String tagName){
        log.info("删除标签，文章id：{},标签名字：{}",id,tagName);
        articleService.addTag(id,tagName);
        return Result.success();
    }
    //根据ids删除文章
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("根据ids删除文章,ids:{}",ids);
        articleService.deleteByIds(ids);
        return Result.success();
    }
    //给文章添加标签
    @PostMapping("/tag")
    public Result addTag(@RequestParam String tagName,@RequestParam Integer id){
        log.info("给文章添加标签，文章id：{}，标签：{}",id,tagName);
        articleService.saveTag(tagName,id);
        return Result.success();
    }
    //档案，获取所有的标签
    @GetMapping("/tag")
    public Result getAllTag(){
        log.info("档案，展示所有的标签");
        List<Tag> tagList = articleService.getAllTag();
        List<String> tagNameList =new ArrayList<>();
        for(Tag tag:tagList){
            tagNameList.add(tag.getTagName());
        }
        return Result.success(tagNameList);
    }
    //根据tagName查询所有文章
    @GetMapping("/tag/{tagName}")
    public Result showArticleByTag(@PathVariable String tagName){
        List<Article> articleList = articleService.getArticleByTag(tagName);
        return Result.success(articleList);
    }

    //较真爬虫，获取id即可，用于测试
    @GetMapping("/crawlJZ")
    public Result crawlJZ(@RequestParam("urlId") String urlId) throws IOException {
        //分析：较真平台的网页数据虽然可以在html标签里获得，但是发现大部分的数据都是在script标签里存储，数据格式为json格式。下面抓取的是script的数据.
        if (!bloomFilterUtils.duplicateCheck(urlId)) {  //如果id不重复
            log.info("爬取id:" + urlId);
            boolean is = crawlerService.crawlJZ(urlId);
            if(is){  //爬取成功
                bloomFilterUtils.urlPut(urlId);
                return Result.success();
            }
        } else {
            log.info("id重复了：" + urlId);
            return Result.error("id重复了");
        }
        bloomFilterUtils.persistBloom(); //数据持久化
        return Result.success();
    }
}

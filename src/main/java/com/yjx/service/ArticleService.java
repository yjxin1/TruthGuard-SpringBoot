package com.yjx.service;

import com.yjx.pojo.Article;
import com.yjx.pojo.PageBean;
import com.yjx.pojo.Result;
import com.yjx.pojo.Tag;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ArticleService {
    //封装article并存入数据库
    Integer saveArticle(Article article);

    //根据文章id，给文章添加一个标签
    void saveTag(String tagName,Integer articleId);

    //根据文章id，保存图片相关信息到数据库
    void saveImage(List<String> urls, List<String> localUrls, Integer articleId);

    //根据文章ID获取文章
    Article getArticleById(Integer id);

    //条件查询，根据标题、发布者、发布时间、爬取来源，鉴定级别，分页展示文章标题、发布者、发布时间
    PageBean page(Integer page, Integer pageSize, String title, String publisher, Short verificationLevel, Short crawlSource, LocalDate begin, LocalDate end,Short status);
    //添加文章并返回文章ID
    Integer add(Article article);

    //修改文章 只允许修改文章标题、上架状态
    void update(Article article);

    //根据文章ID获取标签tags
    List<String> getTagsByArtilceId(Integer articleId);


    //根据ids批量删除文章
    void deleteByIds(List<Integer> ids);

    void addTag(Integer id, String tagName);

    //档案，获取所有的标签
    List<Tag> getAllTag();
    //根据标签名查询带有该标签的文章
    List<Article> getArticleByTag(String tagName);

}

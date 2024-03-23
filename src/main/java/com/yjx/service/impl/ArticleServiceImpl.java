package com.yjx.service.impl;

import com.yjx.mapper.ArticleMapper;
import com.yjx.mapper.ImageMapper;
import com.yjx.mapper.TagMapper;
import com.yjx.pojo.*;
import com.yjx.service.ArticleService;
import com.yjx.utils.HttpUtils;
import com.yjx.utils.TextTools;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private ValueProperties valueProperties;
    @Autowired
    private HttpUtils httpUtils;

    //保存文章到数据库，并返回文章ID，注意只保存文章实体，目前仅用于爬虫
    @Override
    public Integer saveArticle(Article article) {
        articleMapper.insert(article);
        Integer id = articleMapper.getIdByTitle(article.getTitle());
        return id;
    }
    //添加文章,并将文章内的localUrls存入image表，用于前端编写文章
    @Override
    public Integer add(Article article) {
        if(article.getPublishTime()==null){
            article.setPublishTime(LocalDate.now());
        }
        articleMapper.insert(article);
        Integer id = articleMapper.getIdByTitle(article.getTitle());
        List<String> localUrls = TextTools.getLocalUrlsFromText(article.getDebunkText());
        this.saveImage(null,localUrls,id);
        return id;
    }
    //根据文章id添加一个标签
    @Override
    public void saveTag(String tagName, Integer articleId) {
        if(tagName==null||tagName=="") return;
        tagMapper.insertIfNotExists(tagName);  //不存在该标签则新增标签
        Integer tagId = tagMapper.getIdByName(tagName);  //根据标签名获得标签id
        tagMapper.insertRelationIfNotExists(articleId, tagId);
    }

    //根据文章id，在数据库中添加图片的链接
    @Override
    public void saveImage(List<String> urls, List<String> localUrls, Integer articleId) {
        for (int i = 0; i < localUrls.size(); i++) {
            Image image = new Image();
            String localUrl = localUrls.get(i);
            String url = null;
            if(urls!=null){
                url = urls.get(i);
                image.setUrl(url);
            }
            image.setArticleId(articleId);
            image.setLocalUrl(localUrl);
            imageMapper.insert(image);
        }
    }

    //根据文章id删除标签
    @Override
    public void addTag(Integer id, String tagName) {
        Integer tagId = tagMapper.getIdByName(tagName); //获取标签id
        tagMapper.deleteOneTagByArticleId(id, tagId);   //删除标签
    }
    //档案，获取所有的标签
    @Override
    public List<Tag> getAllTag() {
        List<Tag> tagList = tagMapper.getAll();
        return tagList;
    }
    //根据标签名获取带有该标签的文章
    @Override
    public List<Article> getArticleByTag(String tagName) {
        Integer tagId = tagMapper.getIdByName(tagName);
        List<Article> articleList = tagMapper.getArticleByTagId(tagId);
        return articleList;
    }

    //根据文章id获取文章
    @Override
    public Article getArticleById(Integer id) {
        Article article = articleMapper.getById(id);
        return article;
    }

    //分页查询
    @Override
    public PageBean page(Integer page, Integer pageSize, String title, String publisher, Short verificationLevel, Short crawlSource, LocalDate begin, LocalDate end,Short status) {
        Long count = articleMapper.count(title, publisher, verificationLevel, crawlSource, begin, end,status);  //获取总记录数
        log.info("获取总记录数成功");
        Integer start = (page - 1) * pageSize;
        List<Article> articleList = articleMapper.page(start, pageSize, title, publisher, verificationLevel, crawlSource, begin, end,status);  //获取列表数据
        PageBean pageBean = new PageBean(count, articleList);  //封装pageBean对象
        return pageBean;
    }




    //修改文章 只允许修改文章标题、上架状态，未强制
    @Override
    public void update(Article article){
        if(article.getId()!=null){
            articleMapper.update(article);
        }
    }

    //根据文章id获取tags
    @Override
    public List<String> getTagsByArtilceId(Integer articleId) {
        List<Tag> ts = tagMapper.getTagsByArticleId(articleId);
        List<String> tags = new ArrayList<>();
        for (Tag t : ts) {
            tags.add(t.getTagName());
        }
        return tags;
    }



    //根据ids批量删除文章,并删除该文章下的所有图片和标签
    @Transactional  //开启事务
    @Override
    public void deleteByIds(List<Integer> ids) {
        for (Integer id : ids) {  //根据每篇文章，删除本地图片
            List<Image> imageList = imageMapper.getImageByArticleId(id);
            if (imageList != null) {
                imageList.forEach(image -> {  //遍历该文章的imageList，即所有图片
                    File f = new File(valueProperties.getImagePath() + image.getLocalUrl().split("imgName=")[1]);  //得到该篇文章的该条图片的localUrl
                    log.info(f.toString());
                    if (f.exists()) {
                        boolean deleted = f.delete();  // 尝试删除文
                        if (deleted) {           // 检查删除操作是否成功
                            log.info("图片文件删除成功！");
                        } else {
                            log.info("图片文件删除失败！");
                        }
                    } else {
                        log.info("图片文件不存在，无法删除！");
                    }
                });
            }
        }
        articleMapper.deleteByIds(ids);
        imageMapper.deleteByArticleIds(ids);  //从数据库中删除图片
        tagMapper.deleteAllTagByArticleIds(ids);
    }


}

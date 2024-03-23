package com.yjx.mapper;

import com.yjx.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

//包括文章增删改查、评论回复增删改查、文章标签增删改查
@Mapper
public interface ArticleMapper {
    //根据ID查询文章
    @Select("select id, title, rumor_text, debunk_text, publisher, publish_time, summary, crawl_source, verification_level, crawl_url, status from article where id=#{id} and is_deleted=0")
    Article getById(Integer id);
    //插入文章
    @Insert("insert into article(title, rumor_text, debunk_text, publisher, publish_time, summary, crawl_source, verification_level, crawl_url) VALUES"+
            "(#{title},#{rumorText},#{debunkText},#{publisher},#{publishTime},#{summary},#{crawlSource},#{verificationLevel},#{crawlUrl})")
    void insert(Article article);
    //更新文章，只能更新标题和发布状态
    void update(Article article);
    //根据文章名获取文章ID
    @Select("select id from article where title = #{title} and is_deleted=0 limit 1")
    Integer getIdByTitle(String title);

    //根据id删除单个文章
    @Update("update article set is_deleted = 1 where id = #{id}")
    void deleteById(Integer id);

    //根据条件分页查询，返回的article对象只包含id、title、publisher、publishTime、status
    Long count(String title,String publisher,Short verificationLevel,Short crawlSource, LocalDate begin, LocalDate end,Short status);
    List<Article> page(Integer start, Integer pageSize,String title,String publisher,Short verificationLevel,Short crawlSource, LocalDate begin, LocalDate end,Short status);

    //根据Ids批量删除文章
    void deleteByIds(List<Integer> ids);

}

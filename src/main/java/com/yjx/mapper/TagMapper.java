package com.yjx.mapper;

import com.yjx.pojo.Article;
import com.yjx.pojo.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TagMapper {
    //--------标签操作--------
    //查询所有标签
    @Select("select id, tag_name from tag")
    List<Tag> getAll();
    //更改标签名
    @Update("update tag set tag_name=#{tagName}")
    void update(Tag tag);
    //新增不能重复的标签
    void insertIfNotExists(String tagName);
    //删除标签
    @Delete("delete from tag where id=#{id}")
    void delete(Integer id);
    //通过标签名获取标签ID
    @Select("select id from tag where tag_name = #{tagName}")
    Integer getIdByName(String tagName);

    //--------标签中间表操作----------
    //根据文章ID查询对应的标签
    List<Tag> getTagsByArticleId(Integer articleId);
    //根据文章id和标签id，删除文章的一个标签
    @Delete("delete from tag_relation where tag_id=#{tagId} and article_id=#{articleId}")
    void deleteOneTagByArticleId(Integer articleId ,Integer tagId);
    //根据文章ids，批量删除多篇文章的所有标签
    void deleteAllTagByArticleIds(List<Integer> articleIds);
    //给文章新增标签，即插入标签中间表。
    @Insert("insert into tag_relation(article_id, tag_id) values (#{articleId},#{tagId})")
    void insertRelationIfNotExists(Integer articleId,Integer tagId);
    //根据标签id查询所有的文章
    List<Article> getArticleByTagId(Integer tagId);
}

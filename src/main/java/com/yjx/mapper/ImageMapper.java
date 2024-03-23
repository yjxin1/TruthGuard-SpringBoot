package com.yjx.mapper;

import com.yjx.pojo.Image;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ImageMapper {
    //新增图片
    @Insert("insert into image(article_id, url, local_url) values (#{articleId},#{url},#{localUrl})")
    void insert(Image image);

    //根据文章ID删除图片
    void deleteByArticleIds(List<Integer> articleIds);

    //根据文章ID查找图片
    @Select("select id, article_id, url, local_url from image where article_id=#{articleId}")
    List<Image> getImageByArticleId(Integer artcleId);
}

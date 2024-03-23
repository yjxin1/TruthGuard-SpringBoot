package com.yjx.mapper;

import com.yjx.pojo.Article;
import com.yjx.pojo.Clue;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ClueMapper {
    //增删改查
    @Select("select id, link, title, content, submit_time, phone, field from clue where id=#{id}")
    Clue getById(Integer id);
    @Insert("insert into clue(link, title, content, submit_time, phone, field) VALUES"+
            "(#{link},#{title},#{content},#{submitTime},#{phone},#{field})")
    void insert(Clue clue);
    //@Update("update clue set user_id=#{userId},link=#{link}, title=#{title}, content=#{content}, region=#{region}, submit_time=#{submit_time} where id=#{id}")
    void update(Clue clue);
    /*@Delete("delete from clue where id = #{id}")
    void deleteClue(Integer id);*/

    //分页查询
    Long count(String title, String phone, String field, LocalDate begin, LocalDate end);
    List<Clue> page(Integer start, Integer pageSize, String title, String phone, String field, LocalDate begin, LocalDate end);


    //根据Ids批量删除线索
    void deleteByIds(List<Integer> ids);
}

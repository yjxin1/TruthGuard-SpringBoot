package com.yjx.mapper;

import com.yjx.pojo.Question;
import com.yjx.pojo.QuestionRecord;
import com.yjx.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    //增删改查
    @Select("select id, question_text, answer, answer_source, cover from question where id=#{id}")
    Question getById(Integer id);
    @Insert("insert into question(question_text, answer, answer_source, cover)"+
            "values (#{questionText},#{answer},#{answerSource},#{cover})")
    void insert(Question question);
    void update(Question question);
    void deleteByIds(List<Integer> ids);

    //条件分页查询
    Long count(String questionText, Short answer,String answerSource);
    List<Question> page(Integer start, Integer pageSize, String questionText, Short answer,String answerSource);

    //----------答题记录-----------
    @Select("select id, user_id, good_num, question_num, update_time from question_record where user_id=#{userId} order by update_time desc")
    List<QuestionRecord> getRecordListByUserId(Integer userId);
    @Insert("insert into question_record(user_id, good_num, question_num, update_time)"+
            "values (#{userId},#{goodNum},#{questionNum},#{updateTime})")
    void insertRecord(QuestionRecord questionRecord);

    void deleteRecordByIds(List<Integer> ids);

}


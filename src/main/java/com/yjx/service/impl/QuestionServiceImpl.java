package com.yjx.service.impl;

import com.yjx.mapper.QuestionMapper;
import com.yjx.pojo.*;
import com.yjx.service.QuestionService;
import com.yjx.utils.ValueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    protected ValueProperties valueProperties;


    @Override
    public PageBean page(Integer page, Integer pageSize, String questionText, Short answer, String answerSource) {
        Long count = questionMapper.count(questionText, answer, answerSource);  //获取总记录数
        log.info("获取总记录数成功");
        Integer start = (page - 1) * pageSize;
        List<Question> questionList = questionMapper.page(start, pageSize, questionText, answer, answerSource);  //获取列表数据
        PageBean pageBean = new PageBean(count, questionList);  //封装pageBean对象
        return pageBean;
    }

    @Override
    public void add(Question question) {
        questionMapper.insert(question);

    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        for (Integer id : ids) {  //根据每篇文章，删除本地图片
            Question question = questionMapper.getById(id);
            String localUrl = question.getCover();
            if(localUrl!=null){
                File f = new File(valueProperties.getImagePath() + "/questionCover/" + localUrl.split("imgName=")[1]);  //得到该篇文章的该条图片的localUrl
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
            }

        }
        questionMapper.deleteByIds(ids);

    }

    @Override
    public void update(Question question) {
        question.setCover(null);//默认不更新封面
        questionMapper.update(question);
    }

    @Override
    public List<QuestionRecord> getRecordByUserId(Integer userId) {
        List<QuestionRecord> questionRecordList = questionMapper.getRecordListByUserId(userId);
        return questionRecordList;
    }

    @Override
    public void addRecord(QuestionRecord questionRecord) {
        if(questionRecord.getUserId()==null) return;
        if(questionRecord.getUpdateTime()==null){
            questionRecord.setUpdateTime(LocalDateTime.now());
        }
        questionMapper.insertRecord(questionRecord);
    }

    @Override
    public void deleteRecordByIds(List<Integer> ids) {
        questionMapper.deleteRecordByIds(ids);
    }
}

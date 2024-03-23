package com.yjx.service;


import com.yjx.pojo.PageBean;
import com.yjx.pojo.Question;
import com.yjx.pojo.QuestionRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface QuestionService {
    PageBean page(Integer page, Integer pageSize, String questionText, Short answer, String answerSource);
    void add(Question question);

    void deleteByIds(List<Integer> ids);

    void update(Question question);

    //-----答题记录------
    List<QuestionRecord> getRecordByUserId(Integer userId);
    void addRecord(QuestionRecord questionRecord);
    void deleteRecordByIds(List<Integer> ids);
}

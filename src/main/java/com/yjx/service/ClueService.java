package com.yjx.service;

import com.yjx.pojo.Clue;
import com.yjx.pojo.PageBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ClueService {
    //分页查询，根据标题、手机号、类别、发布时间，分页展示线索
    PageBean page(Integer page, Integer pageSize, String title, String phone, String field, LocalDate begin, LocalDate end);
    //提交线索
    void add(Clue clue);
}

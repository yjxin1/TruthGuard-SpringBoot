package com.yjx.service.impl;

import com.yjx.mapper.ClueMapper;
import com.yjx.pojo.Article;
import com.yjx.pojo.Clue;
import com.yjx.pojo.PageBean;
import com.yjx.service.ArticleService;
import com.yjx.service.ClueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Override
    public PageBean page(Integer page, Integer pageSize, String title, String phone, String field, LocalDate begin, LocalDate end) {
        Long count = clueMapper.count(title, phone,field, begin,  end);  //获取总记录数
        log.info("获取总记录数成功");
        Integer start = (page - 1) * pageSize;
        List<Clue> clueList = clueMapper.page(start, pageSize, title, phone,field, begin,  end);  //获取列表数据
        PageBean pageBean = new PageBean(count, clueList);  //封装pageBean对象
        return pageBean;
    }

    @Override
    public void add(Clue clue) {
        if(clue.getSubmitTime()==null){
            clue.setSubmitTime(LocalDate.now());
        }
        clueMapper.insert(clue);
    }
}

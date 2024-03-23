package com.yjx.controller;

import com.yjx.pojo.*;
import com.yjx.service.ClueService;
import com.yjx.service.QuestionService;
import com.yjx.utils.ValueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    //分页展示and条件查询，根据题目内容、回答、来源
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String questionText, Short answer, String answerSource) {
        log.info("文章分页查询，参数：{},{},{},{},{}", page, pageSize, questionText, answer, answerSource);
        PageBean pageBean = questionService.page(page, pageSize, questionText, answer, answerSource);
        return Result.success(pageBean);
    }
    //添加题目
    @PostMapping
    public Result add(@RequestBody Question question) throws IOException {
        log.info("添加题目");
        questionService.add(question);
        return Result.success();
    }

    //批量删除
    @DeleteMapping("/{ids}")
    public Result deleteByIds(@PathVariable List<Integer> ids){
        log.info("批量删除题目ids:{}",ids);
        questionService.deleteByIds(ids);
        return Result.success();
    }
    //修改题目
    @PutMapping
    public Result update(@RequestBody Question question) {
        log.info("更新题目信息：{}", question);
        questionService.update(question);
        return Result.success();
    }
    //-------------答题记录-----------
    //根据用户id获取答题记录
    @GetMapping("/record")
    public Result getRecord(Integer userId){
        log.info("根据用户id获取答题记录：{}",userId);
        List<QuestionRecord> questionRecordList = questionService.getRecordByUserId(userId);
        return Result.success(questionRecordList);
    }
    @PostMapping("/record")
    public Result addRecord(@RequestBody QuestionRecord questionRecord){
        log.info("插入答题记录:{}",questionRecord);
        questionService.addRecord(questionRecord);
        return Result.success();
    }
    @DeleteMapping("/record/{ids}")
    public Result deleteRecordByIds(@PathVariable List<Integer> ids){
        log.info("批量删除{}",ids);
        questionService.deleteRecordByIds(ids);
        return Result.success();
    }

}

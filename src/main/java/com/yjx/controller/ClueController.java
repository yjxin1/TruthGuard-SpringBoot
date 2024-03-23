package com.yjx.controller;

import com.yjx.pojo.Clue;
import com.yjx.pojo.PageBean;
import com.yjx.pojo.Result;
import com.yjx.service.ArticleService;
import com.yjx.service.ClueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/clue")
public class ClueController {
    @Autowired
    private ClueService clueService;

    //分页展示and条件查询，根据标题、手机号、类别、发布时间，分页展示线索
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String title, String phone, String field, LocalDate begin, LocalDate end) {
        log.info("文章分页查询，参数：{},{},{},{},{},{},{}", page, pageSize,title,phone,field, begin, end);
        PageBean pageBean = clueService.page(page, pageSize,title,phone,field, begin, end);
        return Result.success(pageBean);
    }
    //提交线索
    @PostMapping
    public Result add(@RequestBody Clue clue){
        clueService.add(clue);
        return Result.success();
    }
}

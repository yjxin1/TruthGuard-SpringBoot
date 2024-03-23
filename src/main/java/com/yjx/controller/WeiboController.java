package com.yjx.controller;

import com.yjx.mapper.HotMapper;
import com.yjx.pojo.Hot;
import com.yjx.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/weibo")
public class WeiboController {
    @Autowired
    private HotMapper hotMapper;

    @GetMapping()
    public Result listHot(){
        List<Hot> weiboHotList =hotMapper.getNewHot();
        return Result.success(weiboHotList);
    }

}

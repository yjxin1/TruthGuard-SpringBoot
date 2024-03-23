package com.yjx.controller;

import com.yjx.pojo.Result;
import com.yjx.pojo.User;
import com.yjx.service.UserService;
import com.yjx.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    //登录,返回user和token
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        log.info("登录：{}",user);
        User u= userService.login(user);
        u.setPassword(null);//不返回密码
        List<Object> dataList = new ArrayList<>();
        dataList.add(u);
        //登录成功，下发令牌
        if(u!=null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("nick", u.getNick());
            claims.put("type", u.getType());
            String jwt = JwtUtils.generateJwt(claims); //jwt包含了当前员工的信息
            dataList.add(jwt);
            return Result.success(dataList);
        }
        //登陆失败，返回错误信息
        return Result.error("用户名或密码不正确");
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        log.info("注册操作，{}",user);
        if(user.getNick()==null){
            return Result.error("昵称不能为空");
        }
        user.setType((short)1);  //设置用户类型为读者
        User u = userService.login(user);
        if(u!=null){
            return Result.error("已存在该用户");
        }
        userService.add(user);
        return Result.success();
    }
}

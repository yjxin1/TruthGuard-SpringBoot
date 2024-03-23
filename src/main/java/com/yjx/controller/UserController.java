package com.yjx.controller;

import com.yjx.pojo.PageBean;
import com.yjx.pojo.Result;
import com.yjx.pojo.User;
import com.yjx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String nick, String phone, Short type) {
        log.info("用户分页查询，参数：{}，{},{},{},{},{}", page, pageSize, nick, phone, type);
        PageBean pageBean = userService.page(page, pageSize, nick, phone, type);
        return Result.success(pageBean);
    }

    @PutMapping
    public Result update(@RequestBody User user) {
        log.info("更新用户信息：{}", user);
        userService.update(user);
        return Result.success();
    }

//    @DeleteMapping("{id}")
//    public Result delete(Integer id) {
//        log.info("根据id删除用户:{}", id);
//        userService.delete(id);
//        return Result.success();
//    }
    @DeleteMapping("/{ids}")
    public Result deleteByIds(@PathVariable List<Integer> ids){
        log.info("批量删除用户ids:{}",ids);
        userService.deleteByIds(ids);
        return Result.success();
    }

    //添加用户
    @PostMapping
    public Result add(@RequestBody User user) {
        log.info("添加用户{}", user);
        userService.add(user);
        return Result.success();
    }

    @PutMapping("/changePsw")
    public Result changePsw(String phone,String psw,String newPsw){
        User user = new User();
        user.setPhone(phone);
        user.setPassword(psw);
        User u=userService.login(user);
        if(u!=null){  //验证成功
            u.setPassword(newPsw);
            userService.update(u);
        }else{
            return Result.error("密码不正确");
        }
        return Result.success();
    }


}

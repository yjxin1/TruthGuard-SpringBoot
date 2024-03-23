package com.yjx.service;

import com.yjx.pojo.PageBean;
import com.yjx.pojo.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
//用户管理模块（包括用户和核查员）
public interface UserService {
    //登录
    User login(User user);

    PageBean page(Integer page, Integer pageSize, String nick, String phone,Short type);

    void update(User user);

    void delete(Integer id);

    void add(User user);

    void deleteByIds(List<Integer> ids);
}

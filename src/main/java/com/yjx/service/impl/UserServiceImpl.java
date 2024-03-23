package com.yjx.service.impl;

import com.yjx.mapper.UserMapper;
import com.yjx.pojo.PageBean;
import com.yjx.pojo.User;
import com.yjx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override //登录
    public User login(User user) {
        User u = userMapper.getByPhoneAndPassword(user);
        return u;
    }
    @Override //分页查询
    public PageBean page(Integer page, Integer pageSize, String nick, String phone,Short type) {
        Long count = userMapper.count(nick,phone,type);  //获取总记录数
        Integer start = (page - 1) * pageSize;
        List<User> userList = userMapper.page(start, pageSize,nick,phone,type);  //获取列表数据
        PageBean pageBean = new PageBean(count, userList);  //封装pageBean对象
        return pageBean;
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }

    @Override
    public void add(User user) {
        userMapper.insert(user);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        userMapper.deleteByIds(ids);
    }
}

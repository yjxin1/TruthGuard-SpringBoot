package com.yjx.mapper;

import com.yjx.pojo.User;
import com.yjx.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {
    //增删改查
    @Select("select id, nick, phone, password, gender, age, type from user where id=#{id}")
    User getById(Integer id);
    @Insert("insert into user(nick, phone, password, gender, age,type) VALUES"+
            "(#{nick},#{phone},#{password},#{gender},#{age},#{type})")
    void insert(User user);
    void update(User user);


    //条件分页查询
    Long count(String nick, String phone,Short type);
    List<User> page(Integer start, Integer pageSize,String nick, String phone,Short type);

    //根据Ids批量删除线索
    void deleteByIds(List<Integer> ids);

    //根据phone和password查找数据库
    @Select("select id, nick, phone, password, gender, age, type from user where phone =#{phone} and password=#{password}")
    User getByPhoneAndPassword(User user);

    @Delete("delete from user where id=#{id}")
    void delete(Integer id);
}

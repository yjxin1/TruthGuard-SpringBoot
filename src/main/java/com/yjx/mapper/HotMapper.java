package com.yjx.mapper;

import com.yjx.pojo.Hot;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotMapper {
    List<Hot> getNewHot();
    @Insert("insert into hot_weibo(`rank`, `topic`, `link`, `timestamp`, `label`) values (#{rank},#{topic},#{link},#{timestamp},#{label}) " )
    void insert(Hot hot);
}

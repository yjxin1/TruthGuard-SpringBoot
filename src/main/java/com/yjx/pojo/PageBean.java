package com.yjx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*分页查询封装结果类*/
public class PageBean {
    private Long total;//总记录数
    private List rows;//数据列表
}

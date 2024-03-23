package com.yjx.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clue {
    private Integer id;                 // 线索ID
    private String link;                // 链接
    private String title;               // 标题
    private String content;                // 内容
    private String phone;              // 手机号
    private LocalDate submitTime;   // 提交时间
    private String field;   //领域
}


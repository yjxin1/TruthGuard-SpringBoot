package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private Integer id;             // 题目ID
    private String questionText;    // 题目文本
    private Boolean answer;         // 答案，true表示真，false表示假
    private String answerSource;     // 答案来源
    private String cover;  //封面
}

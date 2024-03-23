package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRecord {
    private Integer id;             // ID
    private Integer userId;
    private Integer goodNum;
    private Integer questionNum;
    private LocalDateTime updateTime;
}

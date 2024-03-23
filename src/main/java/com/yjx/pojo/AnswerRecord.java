package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRecord {
    private Integer id;                 // 记录ID
    private Integer userId;           // 读者ID
    private Integer totalAnswers;     // 总答题数量
    private Integer correctAnswers;     // 答对数量
    private LocalDateTime answerTime;   // 答题时间
}



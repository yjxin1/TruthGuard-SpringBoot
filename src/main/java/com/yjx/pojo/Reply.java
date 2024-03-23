package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private Integer id;             // 回复ID
    private Integer commentId;      // 评论ID
    private Integer replyType;       // 回复类型，1表示针对评论的回复，2表示针对回复的回复
    private Integer replyTarget;     // 回复目标，如果replyType是1，则为commentId；如果replyType是2，则为父回复的ID
    private String replyContent;     // 回复内容
    private Integer userId;          // 回复用户ID
}



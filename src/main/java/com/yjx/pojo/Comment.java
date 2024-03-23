package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Integer id;             // 评论ID
    private Integer articleId;      // 文章ID
    private String commentContent;  // 评论内容
    private Integer userId;         // 用户ID
}


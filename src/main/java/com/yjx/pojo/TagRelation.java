package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRelation {
    private Integer id;             // 关系ID
    private Integer articleId;      // 文章ID
    private Integer tagId;          // 标签ID
}

package com.yjx.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopularRecord {
    private Integer id;             // 记录ID
    private Integer articleId;      // 文章ID
    private Integer popularity;     // 热度
    private LocalDateTime updateTime; // 更新时间
}

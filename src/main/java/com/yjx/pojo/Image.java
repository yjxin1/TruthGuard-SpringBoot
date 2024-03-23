package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private Integer id;             // 图片ID
    private Integer articleId;      // 文章ID
    private String url;             // 图片URL
    private String localUrl;        //本地URL
}


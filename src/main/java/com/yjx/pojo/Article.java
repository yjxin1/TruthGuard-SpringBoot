package com.yjx.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Integer id;                     // 文章ID
    private String title;                   // 标题
    private String rumorText;                  // 谣言原文
    private String debunkText;               // 辟谣正文
    private String publisher;        // 发布者
    private LocalDate publishTime;                 // 发布时间，年月日
    private String summary;                  // 查证要点
    private Short crawlSource;            // 爬取来源，1-本系统，2-中互，3-较真，4-果壳
    private Short verificationLevel;      // 鉴定级别,10-真，5半真半假，0-假
    private String crawlUrl;   //爬取的文章url
    private Short status;  //状态：1上架，0未上架
}



package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hot {
    private Integer id;
    private Short rank;
    private String topic;
    private String link;
    private LocalDateTime timestamp;
    private String label;
}

package com.yjx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String nick;
    private String phone;
    private String password;
    private Short gender; //1男，2女
    private Short age;
    private Short type; //1读者，2核查员。3管理员
}

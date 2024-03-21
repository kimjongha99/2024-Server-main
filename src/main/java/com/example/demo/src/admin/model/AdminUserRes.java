package com.example.demo.src.admin.model;


import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRes {

    private  Long id;
    private  String email;
    private  String name;

    private  String lastLoginAt;
    public AdminUserRes(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name= user.getName();
        if (user.getLastLoginAt() != null) {
            this.lastLoginAt = user.getLastLoginAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }


}

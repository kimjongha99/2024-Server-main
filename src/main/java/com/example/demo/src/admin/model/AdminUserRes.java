package com.example.demo.src.admin.model;


import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRes {

    private  Long id;
    private  String email;
    private  String name;


    public AdminUserRes(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name= user.getName();
    }


}

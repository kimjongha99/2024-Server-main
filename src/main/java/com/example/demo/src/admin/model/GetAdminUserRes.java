package com.example.demo.src.admin.model;

import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAdminUserRes {

    List<AdminUserRes> adminUsers;

    Integer listSize;
    Integer totalPage;
    Long totalElements;
    boolean isFirst;
    boolean isLast;


    public GetAdminUserRes(Page<User> users) {
        this.adminUsers = users.map(AdminUserRes::new).getContent();
        this.listSize = users.getNumberOfElements();
        this.totalPage = users.getTotalPages();
        this.totalElements = users.getTotalElements();
        this.isFirst = users.isFirst();
        this.isLast = users.isLast();

    }
}

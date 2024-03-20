package com.example.demo.src.admin.model;

import com.example.demo.common.enums.OAuthProvider;
import com.example.demo.common.enums.UserRoleEnum;
import com.example.demo.common.enums.UserState;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDetailRes {
    private Long id;
    private String email;
    private String name;
    private String birthDate;
    private boolean isOAuth;
    private OAuthProvider oAuthProvider;
    private String lastLoginAt;
    private UserRoleEnum role;
    private UserState state;
    private boolean privacyPolicyAgreed;
    private boolean dataPolicyAgreed;
    private boolean locationBasedServicesAgreed;
    private String lastAgreedAt;

    public AdminUserDetailRes(User user){
        this.id= user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.birthDate = String.valueOf(user.getBirthDate());
        this.isOAuth = user.isOAuth();
        this.oAuthProvider = user.getOAuthProvider();
        this.lastLoginAt = String.valueOf(user.getLastLoginAt());
        this.role = user.getRole();
        this.state = user.getState();
        this.privacyPolicyAgreed = user.isPrivacyPolicyAgreed();
        this.dataPolicyAgreed = user.isDataPolicyAgreed();
        this.locationBasedServicesAgreed = user.isLocationBasedServicesAgreed();
        this.lastAgreedAt = String.valueOf(user.getLastAgreedAt());

    }
}
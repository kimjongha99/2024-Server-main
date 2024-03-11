package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    private String email;
    private String password;
    private String name;

    private boolean isOAuth;

    private boolean privacyPolicyAgreed;

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .isOAuth(this.isOAuth)
                .privacyPolicyAgreed(this.privacyPolicyAgreed)
                .privacyPolicyAgreedAt(this.privacyPolicyAgreed ? LocalDateTime.now() : null)
                .build();
    }
}

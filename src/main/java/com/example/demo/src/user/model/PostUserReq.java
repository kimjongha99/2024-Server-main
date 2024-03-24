package com.example.demo.src.user.model;

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
public class PostUserReq {
    private String email;
    private String password;
    private String name;

    private LocalDate birthDate;

    private boolean privacyPolicyAgreed;
    private boolean locationBasedServicesAgreed;
    private boolean dataPolicyAgreed;


    public User toEntity() {
        return User.userBuilder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .isOAuth(false) // 일반 회원 가입의 경우, 이 값은 항상 false 여야 합니다.
                .birthDate(this.birthDate)
                .privacyPolicyAgreed(this.privacyPolicyAgreed)
                .dataPolicyAgreed(this.dataPolicyAgreed)
                .locationBasedServicesAgreed(this.locationBasedServicesAgreed)
                .build();
    }
}

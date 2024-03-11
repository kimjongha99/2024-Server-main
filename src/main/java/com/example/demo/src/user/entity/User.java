package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.UserRoleEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "USER") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private boolean isOAuth;

    @Column(name = "lastLoginAt")
    private LocalDateTime lastLoginAt; //마지막 로그인

    @Column(nullable = false)
    private boolean privacyPolicyAgreed;  //사용자가 개인정보 처리방침에 동의했는지 여부

    @Column(name = "privacyPolicyAgreedAt")
    private LocalDateTime privacyPolicyAgreedAt; //사용자가 마지막으로 개인정보 처리방침에 동의한 날짜와 시간입니다. 이 필드는 사용자가 동의할 때마다 업데이트


    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", columnDefinition = "VARCHAR(50)")
    private UserRoleEnum role;


    @Builder
    public User(Long id, String email, String password, String name, boolean isOAuth, boolean privacyPolicyAgreed, LocalDateTime privacyPolicyAgreedAt,UserRoleEnum role ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.isOAuth = isOAuth;
        this.privacyPolicyAgreed = privacyPolicyAgreed;
        this.privacyPolicyAgreedAt = privacyPolicyAgreedAt;
        this.role = UserRoleEnum.USER;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
    }


    public void setPrivacyPolicyAgreed(boolean privacyPolicyAgreed) {
        this.privacyPolicyAgreed = privacyPolicyAgreed;
        // 동의 상태에 따라 동의 시간을 현재 시간으로 설정하거나 null로 설정
        this.privacyPolicyAgreedAt = privacyPolicyAgreed ? LocalDateTime.now() : null;
    }
}

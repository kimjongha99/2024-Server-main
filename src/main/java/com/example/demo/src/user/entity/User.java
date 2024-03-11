package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.UserRoleEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(name = "birthDate")
    private LocalDate birthDate; // 생일 필드

    @Column(nullable = false)
    private boolean isOAuth;

    @Column(name = "lastLoginAt")
    private LocalDateTime lastLoginAt; //마지막 로그인
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", columnDefinition = "VARCHAR(50)")
    private UserRoleEnum role;
    @Column(nullable = false)
    private boolean privacyPolicyAgreed;  //사용자가 개인정보 처리방침에 동의했는지 여부
    @Column(nullable = false)
    private boolean dataPolicyAgreed; // 데이터 정책 동의 필드
    @Column(nullable = false)
    private boolean locationBasedServicesAgreed; // 위치기반 서비스 동의 필드

    @Column(name = "lastAgreedAt")
    private LocalDateTime lastAgreedAt; // 마지막으로 동의한 시각





    @Builder
    public User(Long id, String email, String password, String name, boolean isOAuth, boolean privacyPolicyAgreed, LocalDate birthDate, boolean dataPolicyAgreed, boolean locationBasedServicesAgreed, LocalDateTime lastAgreedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.isOAuth = isOAuth;
        this.role = UserRoleEnum.USER;
        this.birthDate = birthDate;
        this.privacyPolicyAgreed = privacyPolicyAgreed;
        this.dataPolicyAgreed = dataPolicyAgreed;
        this.locationBasedServicesAgreed = locationBasedServicesAgreed;
        this.lastAgreedAt= LocalDateTime.now();

    }

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
    }




    //개인정보
    public void setPrivacyPolicyAgreed(boolean privacyPolicyAgreed) {
        this.privacyPolicyAgreed = privacyPolicyAgreed;
    }


    public void setDataPolicyAgreed(boolean dataPolicyAgreed) {
        this.dataPolicyAgreed = dataPolicyAgreed;
    }

    // 위치기반 서비스 동의 상태 설정
    public void setLocationBasedServicesAgreed(boolean locationBasedServicesAgreed) {
        this.locationBasedServicesAgreed = locationBasedServicesAgreed;
    }



    // 마지막으로 동의한 시각 설정
    public void setLastAgreedAt(LocalDateTime lastAgreedAt) {
        this.lastAgreedAt = lastAgreedAt;
    }

    public void setLastLoginAt(LocalDateTime now) {
    this.lastLoginAt=now;
    }


}

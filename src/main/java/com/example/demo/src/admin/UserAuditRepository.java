package com.example.demo.src.admin;

import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuditRepository extends JpaRepository<User, Long> {
    // Envers를 사용하여 변경 이력을 조회하는 메소드 구현
}

//package com.example.demo.src.user;
//
//import com.example.demo.src.user.entity.User;
//import com.example.demo.src.user.model.PostUserReq;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("dev")
//public class UserRepositoryTest {
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void insertOneHundredUsers() {
//        for (int i = 1; i <= 100; i++) {
//            // PostUserReq 객체를 생성합니다.
//            PostUserReq testUser = new PostUserReq("testuser"+i+"@example.com",
//                    "Test1234!",
//                    "Test User"+i,
//                    LocalDate.of(1990, 1, 1),
//                    true,
//                    true,
//                    true);
//
//            // PostUserReq 객체로부터 User 엔티티 객체를 생성합니다.
//            User user = testUser.toEntity();
//
//            // User 엔티티 객체를 데이터베이스에 저장합니다.
//            entityManager.persist(user);
//        }
//        entityManager.flush(); // 데이터베이스에 즉시 반영
//
//        // 검증 로직 - 실제로 100명의 유저가 데이터베이스에 삽입되었는지 확인
//        long userCount = userRepository.count();
//        System.out.println("User Count: " + userCount); // 콘솔에 사용자 수 출력
//    }
//}

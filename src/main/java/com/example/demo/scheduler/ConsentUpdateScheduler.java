package com.example.demo.scheduler;

import com.example.demo.common.service.TimeService;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConsentUpdateScheduler {

    private final UserRepository userRepository;
    private final TimeService timeService;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateConsentStatus() {
        LocalDateTime oneYearAgo = timeService.now().minusYears(1);
        List<User> users = userRepository.findAllByLastAgreedAtBeforeAndDataPolicyAgreedTrueAndLocationBasedServicesAgreedTrue(oneYearAgo);

        for (User user : users) {
            user.setPrivacyPolicyAgreed(false);
            user.setDataPolicyAgreed(false);
            user.setLocationBasedServicesAgreed(false);
            user.setLastAgreedAt(null); // `setLastAgreedAt` 메소드를 `User` 엔티티에 추가해야 합니다.
            userRepository.save(user);
        }
    }

}
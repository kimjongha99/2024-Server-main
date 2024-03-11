package com.example.demo.scheduler;

import com.example.demo.common.service.TimeService;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PrivacyPolicyAgreementScheduler {

    private final UserRepository userRepository;
    private final TimeService timeService;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePrivacyPolicyAgreementStatus() {
        LocalDateTime oneYearAgo = timeService.now().minusYears(1);
        List<User> users = userRepository.findAllByPrivacyPolicyAgreedAndPrivacyPolicyAgreedAtBefore(true, oneYearAgo);

        for (User user : users) {
            user.setPrivacyPolicyAgreed(false);
            userRepository.save(user);
        }
    }
}

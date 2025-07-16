package net.codecraft.jejutrip.common.test;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.user.entity.AuthProvider;
import net.codecraft.jejutrip.user.entity.User;
import net.codecraft.jejutrip.user.entity.UserRole;
import net.codecraft.jejutrip.user.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestUserGenerator implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!userRepository.existsByEmail("codecraft@example.com")) {
            User admin = User.builder()
                    .email("codecraft@example.com")
                    .password(passwordEncoder.encode("jejutrip#2025"))
                    .name("Jeju Trip 관리자")
                    .role(UserRole.ADMIN)
                    .provider(AuthProvider.LOCAL)
                    .build();
            userRepository.save(admin);
        }

        if(!userRepository.existsByEmail("hello@google.com")) {
            User user1 = User.builder()
                    .email("hello@google.com")
                    .password(passwordEncoder.encode("jejutrip#2025"))
                    .role(UserRole.USER)
                    .provider(AuthProvider.LOCAL)
                    .build();
            userRepository.save(user1);
        }
    }
}

package net.codecraft.jejutrip.user.service;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.common.jwt.JwtTokenProvider;
import net.codecraft.jejutrip.user.dto.LoginRequestDto;
import net.codecraft.jejutrip.user.dto.SignupRequestDto;
import net.codecraft.jejutrip.user.dto.TokenResponseDto;
import net.codecraft.jejutrip.user.entity.AuthProvider;
import net.codecraft.jejutrip.user.entity.User;
import net.codecraft.jejutrip.user.entity.UserRole;
import net.codecraft.jejutrip.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("가입되지 않은 이메일입니다.")
                );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        String accessToken =
                jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken =
                jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole());

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void signup(SignupRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .provider(AuthProvider.LOCAL)
                .build();

        userRepository.save(user);
    }
}
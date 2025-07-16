package net.codecraft.jejutrip.user.controller;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.user.dto.LoginRequestDto;
import net.codecraft.jejutrip.user.dto.SignupRequestDto;
import net.codecraft.jejutrip.user.dto.TokenResponseDto;
import net.codecraft.jejutrip.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        TokenResponseDto tokenResponseDto = authService.login(loginRequest);

        return ResponseEntity.ok(tokenResponseDto); // 이 코드가 무슨 뜻이지?
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }
}
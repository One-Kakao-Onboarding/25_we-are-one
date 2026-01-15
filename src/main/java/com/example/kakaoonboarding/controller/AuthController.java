package com.example.kakaoonboarding.controller;

import com.example.kakaoonboarding.config.SessionUser;
import com.example.kakaoonboarding.dto.request.LoginRequest;
import com.example.kakaoonboarding.dto.response.LoginResponse;
import com.example.kakaoonboarding.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 로그인
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            LoginResponse response = authService.login(request, session);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            LoginResponse errorResponse = new LoginResponse("error", e.getMessage(), null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 로그아웃
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        authService.logout(session);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        try {
            SessionUser user = authService.getCurrentUser(session);
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getRole(),
                    user.getEmployeeId(),
                    user.getDepartment(),
                    user.getCompanyId()
            );
            return ResponseEntity.ok(userInfo);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}

package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.config.SessionUser;
import com.example.kakaoonboarding.dto.request.LoginRequest;
import com.example.kakaoonboarding.dto.response.LoginResponse;
import com.example.kakaoonboarding.entity.User;
import com.example.kakaoonboarding.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private static final String SESSION_USER_KEY = "USER";

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 로그인
     */
    public LoginResponse login(LoginRequest request, HttpSession session) {
        // 사용자 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 비밀번호 확인 (평문 비교 - 실제로는 암호화 필요)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        // 세션에 사용자 정보 저장
        SessionUser sessionUser = new SessionUser(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole(),
                user.getEmployeeId(),
                user.getDepartment(),
                user.getCompanyId()
        );
        session.setAttribute(SESSION_USER_KEY, sessionUser);

        // 응답 생성
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole(),
                user.getEmployeeId(),
                user.getDepartment(),
                user.getCompanyId()
        );

        return new LoginResponse("success", "로그인 성공", userInfo);
    }

    /**
     * 로그아웃
     */
    public void logout(HttpSession session) {
        session.invalidate();
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public SessionUser getCurrentUser(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_KEY);
        if (user == null) {
            throw new RuntimeException("로그인이 필요합니다");
        }
        return user;
    }

    /**
     * 로그인 여부 확인
     */
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }
}

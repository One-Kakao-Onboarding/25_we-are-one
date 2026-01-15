package com.example.kakaoonboarding.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 인증 체크 인터셉터
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String SESSION_USER_KEY = "USER";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 요청은 통과 (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 로그인, 로그아웃, H2 콘솔, 거리 계산 API는 인증 제외
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/auth/") ||
            requestURI.startsWith("/h2-console") ||
            requestURI.equals("/api/distance")) {
            return true;
        }

        // 세션에서 사용자 정보 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SESSION_USER_KEY) == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"로그인이 필요합니다\"}");
            return false;
        }

        return true;
    }
}

package com.example.MyPersonalContactManager.infrastructure;

//import com.example.MyPersonalContactManager.service.TokenValidationService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

//@Component
//public class AuthenticationInterceptor implements HandlerInterceptor {
//    private final TokenValidationService tokenValidationService;
//
//    public AuthenticationInterceptor(TokenValidationService tokenValidationService) {
//        this.tokenValidationService = tokenValidationService;
//    }
//
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler) throws Exception {
//        String token = request.getHeader("Authorization");
//        if (token == null || !tokenValidationService.validateToken(token)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return false;
//        }
//        return true;
//    }
//}

@Component
public class AuthInterceptor {
    public String getTokenExtraction(HttpServletRequest request) {
        // Извлекаем JWT-токен из заголовка Authorization
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Убираем "Bearer " из строки
        }
        return token;
    }
}



package com.example.MyPersonalContactManager.infrastructure;

//import com.example.MyPersonalContactManager.service.TokenValidationService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

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



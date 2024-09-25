package com.example.MyPersonalContactManager.infrastructure;

//import com.example.MyPersonalContactManager.service.TokenValidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class AuthInterceptor {
    private final String secretKey = "5Hdo5+PxMJkLQ9Wo7WnYMR/gBzTfC5XrB3iNPvMlscY=";
    private final byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);

    public String getTokenExtraction(HttpServletRequest request) {
        // Извлекаем JWT-токен из заголовка Authorization
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Убираем "Bearer " из строки
        }
        return token;
    }

    public int extractUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes) // Передаем байтовый массив
                .parseClaimsJws(token)
                .getBody();

        return (int) claims.get("userId");
    }

    public String extractUserRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("role");
    }
}



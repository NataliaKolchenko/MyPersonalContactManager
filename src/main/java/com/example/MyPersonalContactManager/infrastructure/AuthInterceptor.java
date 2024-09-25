package com.example.MyPersonalContactManager.infrastructure;

//import com.example.MyPersonalContactManager.service.TokenValidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

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

    public int extractUserIdFromToken(String token) {
        // Проверяем строку и дополняем недостающими символами =, если нужно
        String secretKey = "5Hdo5+PxMJkLQ9Wo7WnYMR/gBzTfC5XrB3iNPvMlscY=";
        if (secretKey.length() % 4 != 0) {
            secretKey = secretKey + "=".repeat(4 - (secretKey.length() % 4)); // Добавляем = для выравнивания длины
        }

        // Декодируем Base64-строку в байтовый массив
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);

        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes) // Передаем байтовый массив
                .parseClaimsJws(token)
                .getBody();

        return (int) claims.get("userId");
    }
}



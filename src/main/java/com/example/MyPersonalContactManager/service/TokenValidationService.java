package com.example.MyPersonalContactManager.service;

import com.example.MyPersonalContactManager.exceptions.ValidateTokenException;
import com.example.MyPersonalContactManager.infrastructure.AuthInterceptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class TokenValidationService {
    @Autowired
    private AuthInterceptor authInterceptor;

    private final String secretKey = "5Hdo5+PxMJkLQ9Wo7WnYMR/gBzTfC5XrB3iNPvMlscY=";

    public boolean validateToken(String token) {
        try {
            // Парсим и проверяем токен с использованием секретного ключа
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(secretKey))
                    .parseClaimsJws(token);

            // Проверяем, что токен не просрочен
            Date expiration = claimsJws.getBody().getExpiration();
            return expiration != null && expiration.after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            // Если токен недействителен или подпись неверна
            e.getMessage();
            return false;
        }
    }

    public void validateRequestToken(HttpServletRequest request) {
        String token = authInterceptor.getTokenExtraction(request);
        boolean isValid = validateToken(token);
        if (!isValid) {
            throw new ValidateTokenException("Unauthorized: Invalid or missing token");
        }
    }
}

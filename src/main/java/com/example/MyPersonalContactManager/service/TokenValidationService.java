package com.example.MyPersonalContactManager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class TokenValidationService {
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
}

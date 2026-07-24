package com.finovoria.finastrax.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.finovoria.finastrax.entity.AppUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "finastraxsecretkeyfinastraxsecretkeyfinastraxsecretkey123456"
            .getBytes()
    );

    private final long EXPIRATION_TIME = 1000 * 60 * 60;


    public String generateToken(AppUser user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getUserId().toString());
        claims.put("tenantId", user.getTenant().getTenantId());
        claims.put("databaseName", user.getTenant().getDatabaseName());
        claims.put("role", user.getRole().name());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
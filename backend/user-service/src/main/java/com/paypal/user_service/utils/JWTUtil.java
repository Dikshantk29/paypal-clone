package com.paypal.user_service.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/*
* Signup creates an account.
Login verifies the account's credentials and generates a token.
JwtUtil creates and reads JWT tokens.
JwtRequestFilter checks incoming requests for a
JWT and establishes authentication when appropriate.*/


@Component
public class JWTUtil {


    private static final String SECRET = "secret123secret123secret123secret123secret123secret123";


    private Key getSigninKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());

    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            extractEmail(token);//if parsing succeeds, token is valid
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))//1 day
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}

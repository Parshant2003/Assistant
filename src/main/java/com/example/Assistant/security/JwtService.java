package com.example.Assistant.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Ye class token ki "factory + verifier" hai.
 *
 * Kaise samjhein: JWT teen parts ka hota hai - header.payload.signature
 * Payload me hum email + expiry daalte hain. Signature ek secret key se
 * bani hoti hai - agar koi token ka payload chhed de, signature match
 * nahi karega aur hum reject kar denge. Isi wajah se server ko DB me
 * session check karne ki zaroorat nahi padti - signature hi trust ka
 * source hai.
 */
@Component
public class JwtService {

    // Production me ye env variable se aana chahiye, hardcode nahi.
    // Yahan config se aa raha hai (application.yml dekho).
    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // yahan email store ho raha hai
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // read the subject: email
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    // extractClaim() token ko open + verify + data return karta hai.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
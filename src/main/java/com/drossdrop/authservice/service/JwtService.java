package com.drossdrop.authservice.service;

import com.drossdrop.authservice.controller.AuthController;
import com.drossdrop.authservice.entity.UserCredential;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }


    public String generateToken(UserCredential user) {
        Set<String> roles = new HashSet<>(
                Collections.singletonList(user.getRoleName())
        );
        return createToken(user, roles);
    }

    private String createToken(UserCredential user, Set<String> roles) {
        return Jwts.builder()
                .setSubject(String.format("%s", user.getId()))
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getSubjectFromToken(String token) {
        Claims claims = decodeToken(token);
        LOGGER.info("====== Claims value: " + claims);
        return claims.getSubject();
    }

    public Claims decodeToken(String token) {
        Jws<Claims> parsedToken = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);

        return parsedToken.getBody();
    }
}

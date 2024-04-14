package com.anuki.booklovers.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${book-lovers.app.jwtSecret}")
    private String secretKey;

    @Value("${book-lovers.app.jwtExpirationMs}")
    private long jwtExpirationInMillis;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Optional<Boolean> validateToken(String token, UserDetails userDetails) {
        return getUsernameFromToken(token)
                .map(username -> username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration)
                .orElse(new Date(0));
    }

    private <T> Optional<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.ofNullable(claimsResolver.apply(claims));
        } catch (Exception e) {
            log.error("Error extracting JWT claim: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Optional<String> getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}

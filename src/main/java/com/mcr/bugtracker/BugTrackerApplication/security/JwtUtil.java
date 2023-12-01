package com.mcr.bugtracker.BugTrackerApplication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ConfigurationProperties("security.jwt.token")
@Data
public class JwtUtil implements Serializable {

    @Value("${security.jwt.token.secret_key}")
    private String secret_key;
//    private byte[] secretKeyBytes = secret_key.getBytes(StandardCharsets.UTF_8);
//    private SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
    @Value("${security.jwt.token.expire_length}")
    private long expire_length;
//    private SecurityJWTTokenProperties jwtTokenProperties;

    public JwtUtil() throws NoSuchAlgorithmException {
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        extraClaims.put("role", userDetails.getAuthorities().stream().findFirst().orElseThrow());
        return buildToken(extraClaims, userDetails, expire_length);
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret_key)
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey() {
        byte[] secretKeyBytes = secret_key.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey.toString().replaceAll("[^a-zA-Z0-9]", "0"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
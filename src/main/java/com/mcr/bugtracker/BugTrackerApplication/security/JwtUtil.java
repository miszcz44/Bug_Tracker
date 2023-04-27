package com.mcr.bugtracker.BugTrackerApplication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
public class JwtUtil implements Serializable {

//    @Value("${application.security.jwt.token.secret-key}")
    private String secretKeyString = "aaaaa";
    private byte[] secretKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
    private SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
//    @Value("${application.security.jwt.token.expire-length}")
    private long jwtExpiration = 100000000;

    public JwtUtil() throws NoSuchAlgorithmException {
    }
//    @Value("${application.security.jwt.refresh-token.expiration}")
//    private long refreshExpiration;

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
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

//    public String generateRefreshToken(
//            UserDetails userDetails
//    ) {
//        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
//    }

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
                .signWith(SignatureAlgorithm.HS512, secretKeyString)
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
                .setSigningKey(secretKeyString)
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        secretKey.toString().replace(".", "-");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey.toString().replaceAll("[^a-zA-Z0-9]", "0"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
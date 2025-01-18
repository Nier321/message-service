package com.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class Jwt2Util {
    private PrivateKey privateKey; // 用于签名的私钥
    private PublicKey publicKey; // 用于验证的公钥

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1小时有效期
                .signWith(SignatureAlgorithm.RS256, privateKey) // 使用 RSA 签名
                .compact();
    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}

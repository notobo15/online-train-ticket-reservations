package com.trainticketbooking.app.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;  // Đây sẽ là chuỗi Base64

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Tạo JWT từ username (hoặc các thông tin cần thiết khác)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, key())  // Sử dụng key() để tạo Key hợp lệ
                .compact();
    }

    // Lấy username từ JWT
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()  // Sử dụng parserBuilder() thay cho parser()
                .setSigningKey(key())  // Giải mã Key từ jwtSecret
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Kiểm tra xem token có hợp lệ không
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()  // Sử dụng parserBuilder() để thay thế parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Giải mã Base64 jwtSecret để tạo Key hợp lệ cho HMAC
    private Key key() {
        // Giải mã jwtSecret từ chuỗi Base64
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());  // Sử dụng getBytes thay cho Base64.decode() nếu jwtSecret là chuỗi thô
    }
}

package com.websystique.springboot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 使用用户名生成对应的token
     * @param userName
     * @return
     */
    public String generate(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + 600 * 1000))
                .signWith(key)
                .compact();
    }

    public boolean valid(String token) {
        if (null != getUsername(token) && isExpired(token)) {
            return true;
        }
        return  false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    /**
     * jwt从token中获取用户信息
     * @param token
     * @return
     */
    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

}

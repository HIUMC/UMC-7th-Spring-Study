package com.example.springjwt.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey; // 객체 키 저장

    // @Value 어노테이션을 통해 application.properties에 저장되어 있는 secret key를 string 타입 변수로 받을 것임
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        // 받은 secret key를 객체 변수로 암호화
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /* ---검증을 진행할 메서드--- */
    // 각각의 메서드는 토큰을 전달받아 내부의 JWT parser를 이용해 데이터 확인

    public String getUsername(String token) {

        // verifyWith를 통해 암호화된 토큰이 이 서버에서 생성되었는지 검증
        // payload에서 get을 통해 string 타입의 특정 데이터 획득, 이 데이터는 username이라는 키 가짐
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    /*--- 로그인 성공시 토큰을 생성할 메서드 ---*/

    public String createJwt(String username, String role, Long expiredMs) { // username, role, 토큰의 유효시간을 인자로 받음

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // issuedAt을 통해 토큰의 현재 발행 시간 넣어줌
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰이 언제 소멸될 것인지 (현재 발행시간 + 토큰 유효시간)
                .signWith(secretKey)
                .compact();
    }
}

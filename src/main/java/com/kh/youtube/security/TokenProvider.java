package com.kh.youtube.security;

import com.kh.youtube.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {
    private static final String SECRET_KEY="FlRpX30MqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M10icegkpi46AdvrcX1E6CmTUBc6IFbTPiD";

    public String create(Member member) {
        // 토큰 생성 -> 기한 지정 가능(1일)
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // build.gradle에서 라이브러리 추가 후
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // header에 들어갈 내용
                .setSubject(member.getId()) // payload에 들어갈 내용
                .setIssuer("Youtube app")
                .setIssuedAt(new Date()) // 현재부터
                .setExpiration(expiryDate) // 기한까지
                .compact();
    }

    public String validateAndGetUserId(String token){
        // token을 디코딩, 파싱 및 위조해서 보냈는지 검증하는 메소드

        Claims claims = Jwts.parser()
                            .setSigningKey(SECRET_KEY)
                            .parseClaimsJws(token)
                            .getBody(); // decode 할 내용이 상단 메소드의 member.getId() 부분인데 이 부분은 body에 있음
        return claims.getSubject(); // id를 반환

    }

}

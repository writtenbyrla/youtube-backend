package com.kh.youtube.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 한 번만 인증하는 필터

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 토큰 가져오기
        String token = parseBearerToken(request);

        // 토큰 검사
        if(token!=null && !token.equalsIgnoreCase("null")){ // null이 아닌 것
            // Member -> id (TokenProvider의 validateAndGetUserId 메서드 반환값(id) 가져옴
            String id = tokenProvider.validateAndGetUserId(token);

            // 인증 완료 SecurityContextHolder에 등록
            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    id, // 인증된 사용자 정보
                    null,
                    AuthorityUtils.NO_AUTHORITIES
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7); // "Bearer "가 7글자라 8부터 가지고 와야하는데 index는 0부터니까 7
        }
        return null;
    }
}

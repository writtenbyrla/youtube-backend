package com.kh.youtube.controller;

import com.kh.youtube.domain.Member;
import com.kh.youtube.domain.MemberDTO;
import com.kh.youtube.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.kh.youtube.security.TokenProvider;

@Slf4j
@RestController
@RequestMapping("/auth")
public class MemberController {

    @Autowired
    private MemberService service;

    @Autowired
    private TokenProvider tokenProvider;

    // 회원가입시 패스워드 암호화 처리
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // Member를 token에 담아서 써야 하기 때문에 MemberDTO 생성
    // 회원가입
    // http://localhost:8080/auth/signup
    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody MemberDTO dto){

        // 비밀번호 -> 암호화 처리 + 저장할 유저 만들기
        Member member = Member.builder()
                                .id(dto.getId())
                                .password(passwordEncoder.encode(dto.getPassword()))
                                .name(dto.getName())
                                .build();

        // 서비스를 이용해 repository에 유저 저장
        Member registerMember = service.create(member);

        // 그대로 보내면 password 노출되기 때문에
        MemberDTO responseDTO = dto.builder()
                                    .id(registerMember.getId())
                                    .name(registerMember.getName())
                                    .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 로그인 -> token
    @PostMapping("/signin")
    public ResponseEntity authenticate(@RequestBody MemberDTO dto){
        Member member = service.getByCredentials(dto.getId(), dto.getPassword(), passwordEncoder);

        if(member!=null){ // -> 토큰 생성
            String token = tokenProvider.create(member);
            MemberDTO responseDTO = MemberDTO.builder()
                                                .id(member.getId())
                                                .name(member.getName())
                                                .token(token)
                                                .build();
            return ResponseEntity.ok().body(responseDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
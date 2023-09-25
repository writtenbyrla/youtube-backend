package com.kh.youtube.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder // 기존 데이터를 하나씩 입력 가능
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    // MemberController에서 Member 내용을 token에 담아서 써야 하기 때문에 MemberDTO 생성
    private String token;
    private String id;
    private String password;
    private String name;
}

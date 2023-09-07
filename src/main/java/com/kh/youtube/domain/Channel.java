package com.kh.youtube.domain;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
	private int channelCode;
	private String channelName;
	private String channelDesc;
	private Date channelDate;
	
	// 다대일 관계(한 멤버가 여러 채널을 가지고 있음)
	@ManyToOne // Channel 엔티티와 Member 엔티티를 다대일 관계로 설정
	@JoinColumn(name="member_id") // join되는 컬럼명(Member(primary key) - Channel(foreign key)) 명시 // 외래키 생성 or Member 엔티티의 기본키와 매핑
	private Member member;

	
}

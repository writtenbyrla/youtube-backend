package com.kh.youtube.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	@Id //primary key
	@Column(name="category_code") // table의 column명과 필드명이 일치하지 않는경우 일치하게 설정
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "categorySequence") // 대표값을 자동으로 생성(시퀀스와 관련)
	@SequenceGenerator(name="categorySequence", sequenceName = "SEQ_CATEGORY", allocationSize = 1) // 1씩 증가시킴
	private int categoryCode;
	
	@Column(name="category_name") // primary key가 아닌 필드
	private String categoryName;

	
}

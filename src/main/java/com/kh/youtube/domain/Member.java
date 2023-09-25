package com.kh.youtube.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert // default값 자동으로 추가
@Builder
public class Member {

	@Id // primary key
	private String id;

	@Column
	private String password;

	@Column
	private String name;

	@Column
	private String authority;

}

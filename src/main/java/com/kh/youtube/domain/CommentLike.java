package com.kh.youtube.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
// @Table(name="COMMENT_LIKE") // 테이블명이 일치하지 않아서 오류나는 경우 테이블명 명시해서 해결
public class CommentLike {
	@Id
	@Column(name="comm_like_code")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "commentLikeSequence")
	@SequenceGenerator(name="commentLikeSequence", sequenceName = "SEQ_COMMENT_LIKE", allocationSize = 1)
	private int commLikeCode;

	@Column(name="comm_like_date")
	private Date commLikeDate;

	@ManyToOne
	@JoinColumn(name="comment_code")
	private VideoComment videoComment;

	@ManyToOne
	@JoinColumn(name="id")
	private Member member;
	
}

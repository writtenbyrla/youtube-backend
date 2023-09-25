package com.kh.youtube.repo;

import com.kh.youtube.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDAO extends JpaRepository<Member, String> { // primary key의 데이터 타입

    Member findByIdAndPassword(String id, String password);
}

package com.kh.youtube.repo;

import com.kh.youtube.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscribeDAO extends JpaRepository<Subscribe, Integer> {

    // 내가 구독한 채널 보기
    // SELECT * FROM subscribe WHERE id=?
    @Query(value="SELECT * FROM subscribe WHERE id= :id", nativeQuery = true)
    List<Subscribe> findByMemberId(String id);
}

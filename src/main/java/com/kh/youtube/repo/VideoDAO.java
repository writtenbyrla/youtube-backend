package com.kh.youtube.repo;

import com.kh.youtube.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoDAO extends JpaRepository<Video, Integer> {

    // 채널별 영상 목록
    @Query(value="SELECT * FROM video WHERE channel_code = :channelCode", nativeQuery = true)
    List<Video> findByChannelCode(int code);
}

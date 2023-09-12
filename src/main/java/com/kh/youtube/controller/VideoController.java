package com.kh.youtube.controller;

import com.kh.youtube.domain.CommentLike;
import com.kh.youtube.domain.Video;
import com.kh.youtube.domain.VideoComment;
import com.kh.youtube.domain.VideoLike;
import com.kh.youtube.service.CommentLikeService;
import com.kh.youtube.service.VideoCommentService;
import com.kh.youtube.service.VideoLikeService;
import com.kh.youtube.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/*")
public class VideoController {

    @Autowired
    private VideoService video;

    @Autowired
    private VideoCommentService videoComment;

    @Autowired
    private VideoLikeService videoLike;

    @Autowired
    private CommentLikeService commentLike;


    // 영상 전체 조회 : GET - http://localhost:8080/api/video
    @GetMapping("/video")
    public ResponseEntity<List<Video>> showAllVideo(){
        return ResponseEntity.status(HttpStatus.OK).body(video.showAll());
    }
    
    // 영상 추가 : POST - http://localhost:8080/api/video
    @PostMapping("/video")
    public ResponseEntity<Video> createVideo(@RequestBody Video vo){
        return ResponseEntity.status(HttpStatus.OK).body(video.create(vo));
    }

    // 영상 수정 : PUT - http://localhost:8080/api/video
    @PutMapping("/video")
    public ResponseEntity<Video> updateVideo(@RequestBody Video vo){
        return ResponseEntity.status(HttpStatus.OK).body(video.update(vo));
    }

    // 영상 삭제 : DELETE - http://localhost:8080/api/video/1
    @DeleteMapping("/video/{id}")
    public ResponseEntity<Video> deleteVideo(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(video.delete(id));
    }
    
    // 영상 1개 조회 : GET - http://localhost:8080/api/video/1
    @GetMapping("/video/{id}")
    public ResponseEntity<Video> showVideo(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(video.show(id));
    }
    
    // 영상 1개에 따른 댓글 전체 조회 : GET - http://localhost:8080/api/video/1/comment
    @GetMapping("/video/{id}/comment")
    public ResponseEntity<List<VideoComment>> showAllComment(@PathVariable int id){
        // videoCommentDAO, Service에 쿼리문 추가
        return ResponseEntity.status(HttpStatus.OK).body(videoComment.findByVideoCode(id));
    }
    
    // 좋아요 추가 : POST - http://localhost:8080/api/video/like
    @PostMapping("/video/like")
    public ResponseEntity<VideoLike> createVideoLike(@RequestBody VideoLike vo){
        return ResponseEntity.status(HttpStatus.OK).body(videoLike.create(vo));
    }
    
    // 좋아요 취소 : DELETE - http://localhost:8080/api/video/like/1
    @DeleteMapping("/video/like/{id}")
    public ResponseEntity<VideoLike> deleteVideoLike(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(videoLike.delete(id));
    }
    
    // 댓글 추가 : POST - http://localhost:8080/api/video/comment
    @PostMapping("video/comment")
    public ResponseEntity<VideoComment> createVideoComment(@RequestBody VideoComment vo){
        return ResponseEntity.status(HttpStatus.OK).body(videoComment.create(vo));
    }
    
    // 댓글 수정 : PUT - http://localhost:8080/api/video/comment
    @PutMapping("video/comment")
    public ResponseEntity<VideoComment> updateVideoComment(@RequestBody VideoComment vo){
        return ResponseEntity.status(HttpStatus.OK).body(videoComment.update(vo));
    }
    
    // 댓글 삭제 : DELETE - http://localhost:8080/api/video/comment/1
    @DeleteMapping("video/comment/{id}")
    public ResponseEntity<VideoComment> deleteVideoComment(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(videoComment.delete(id));
    }
    
    // 댓글 좋아요 추가 : POST - http://localhost:8080/api/video/comment/like
    @PostMapping("video/comment/like")
    public ResponseEntity<CommentLike> createCommentLike(@RequestBody CommentLike vo){
        return ResponseEntity.status(HttpStatus.OK).body(commentLike.create(vo));
    }
    
    // 댓글 좋아요 취소 : DELETE - http://localhost:8080/api/video/comment/like/1
    @DeleteMapping("/video/comment/like/{id}")
    public ResponseEntity<CommentLike> deleteCommentLike(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(commentLike.delete(id));
    }
}

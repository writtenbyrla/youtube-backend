package com.kh.youtube.controller;

import com.kh.youtube.domain.*;
import com.kh.youtube.service.CommentLikeService;
import com.kh.youtube.service.VideoCommentService;
import com.kh.youtube.service.VideoLikeService;
import com.kh.youtube.service.VideoService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/*")
@Log4j2
@CrossOrigin(origins={"*"}, maxAge=6000) // 리액트랑 연결
public class VideoController {

    @Value("${youtube.upload.path}") // application.properties에 있는 변수(업로드된 파일의 경로)
    private String uploadPath;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoCommentService videoComment;

    @Autowired
    private VideoLikeService videoLike;

    @Autowired
    private CommentLikeService commentLike;


    // 영상 전체 조회 : GET - http://localhost:8080/api/video
    // 페이지 처리 후 : GET - http://localhost:8080/api/video?page=1
    // (@RequestParam(name="page", defaultValue = "1") int page 넣어주면 default값 1 이라서 video까지만 주소 넣어도 목록 나옴

    @GetMapping("/video")
    public ResponseEntity<List<Video>> showAllVideo(@RequestParam(name="page", defaultValue = "1") int page,  @RequestParam(name="category", required = false) Integer category){
        // Integar인 경우 null값 넣을 수 있음

        // 정렬
        Sort sort = Sort.by("videoCode").descending();
        // 한 페이지에 영상 10개
        Pageable pageable = PageRequest.of(page-1, 20, sort); // 시작페이지(0부터 시작), 몇개씩 보여줄지

        //동적 쿼리를 위한 QueryDSL을 사용한 코드들 추가
        //1. Q도메인 클래스를 가져와야 한다.
        QVideo qVideo = QVideo.video;

        //2. BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        if(category!=null) {
            //3.원하는 조건은 필드값과 함께 결합해서 생성하기
            BooleanExpression expression = qVideo.category.categoryCode.eq(category);
            //4.만들어진 조건은 where문에 and 나 or 같은 키워드와 결합한다.
            builder.and(expression);
        }

        Page<Video> result =  videoService.showAll(pageable, builder);


        log.info("Total Pages : " + result.getTotalPages()); // 총 몇 페이지
        log.info("Total Count : " + result.getTotalElements()); // 전체 몇 페이지
        log.info("Page Number : " + result.getNumber()); // 현재 페이지 번호
        log.info("Page size : " + result.getSize()); // 페이지 당 데이터 개수
        log.info("Next Page : " + result.hasNext()); // 다음 페이지가 있는지 존재여부
        log.info("First Page : " + result.isFirst()); // 시작 페이지 여부

        //return ResponseEntity.status(HttpStatus.OK).build();
        //return ResponseEntity.status(HttpStatus.OK).body(videoService.showAll(pageable); 반환타입 안맞음
        return ResponseEntity.status(HttpStatus.OK).body(result.getContent());
    }
    
    // 영상 추가 : POST - http://localhost:8080/api/video
    // @RequestParam(name="desc", required = false) String desc 필수 값이 아닌 경우
    @PostMapping("/video")
    public ResponseEntity<Video> createVideo(MultipartFile video, MultipartFile image, String title, @RequestParam(name="desc", required = false) String desc, String categoryCode){

        // video_title, video_desc, video_url, video_photo, category_code
        // 파일로 가지고 올 때 @RequestBody 대신 MultipartFile(postman에서 key값과 맞추기)
        log.info("video : " + video);
        log.info("image : " + image);
        log.info("title : " + title);
        log.info("desc : " + desc);
        log.info("categoryCode : " + categoryCode);

        // 업로드 처리
        // 비디오의 실제 파일 이름
        String originalVideo = video.getOriginalFilename();
        log.info("original : " + originalVideo); // original : video (1).mp4 // 앞에 경로까지 붙어서 나오는 경우 있음
        String realVideo = originalVideo.substring(originalVideo.lastIndexOf("\\")+1);
        log.info("realVideo : " + realVideo);

        // 이미지 실제 파일 이름
        String originalImage = image.getOriginalFilename();
        String realImage = originalImage.substring(originalImage.lastIndexOf("\\")+1);

        // UUID
        String uuid = UUID.randomUUID().toString();

        // 비디오 실제로 저장할 파일 명(위치 포함)
        String saveVideo = uploadPath + File.separator + uuid + "_" + realVideo;
        log.info("saveVideo : " + saveVideo);
        Path pathVideo = Paths.get(saveVideo);
        try {
            video.transferTo(pathVideo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 이미지 실제로 저장할 파일 명
        String saveImage = uploadPath + File.separator + uuid + "_" + realImage;
        Path pathImage = Paths.get(saveImage);
        try {
            image.transferTo(pathImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // db에 추가 ( title, desc, video, image, categorycode, channelcode, member)
        Video vo = new Video();
        vo.setVideoTitle(title);
        vo.setVideoDesc(desc);

        //vo.setVideoUrl(saveVideo); // 절대경로일 경우
        vo.setVideoUrl(uuid + "_" + realVideo);
        // vo.setVideoPhoto(saveImage); 절대경로일 경우
        vo.setVideoPhoto(uuid + "_" + realImage);

        Member member = new Member();
        member.setId("mango");
        vo.setMember(member);

        Category category = new Category();
        category.setCategoryCode(Integer.parseInt(categoryCode));
        vo.setCategory(category);

        Channel channel = new Channel();
        channel.setChannelCode(22);
        vo.setChannel(channel);

        //return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.OK).body(videoService.create(vo));
    }

    // 영상 수정 : PUT - http://localhost:8080/api/video
    @PutMapping("/video")
    public ResponseEntity<Video> updateVideo(@RequestBody Video vo){
        return ResponseEntity.status(HttpStatus.OK).body(videoService.update(vo));
    }

    // 영상 삭제 : DELETE - http://localhost:8080/api/video/1
    @DeleteMapping("/video/{id}")
    public ResponseEntity<Video> deleteVideo(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(videoService.delete(id));
    }
    
    // 영상 1개 조회 : GET - http://localhost:8080/api/video/1
    @GetMapping("/video/{id}")
    public ResponseEntity<Video> showVideo(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(videoService.show(id));
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

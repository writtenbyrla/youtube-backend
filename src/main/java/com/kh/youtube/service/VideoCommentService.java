package com.kh.youtube.service;

import com.kh.youtube.domain.Subscribe;
import com.kh.youtube.domain.VideoComment;
import com.kh.youtube.repo.VideoCommentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoCommentService {

    @Autowired
    private VideoCommentDAO dao;

    public List<VideoComment> showAll(){
        return dao.findAll();
    }

    public VideoComment show(int id){
        return dao.findById(id).orElse(null);
    }

    public VideoComment create(VideoComment vo){
        return dao.save(vo);
    }

    public VideoComment update(VideoComment vo){
        VideoComment target = dao.findById(vo.getCommentCode()).orElse(null);

        if(target!=null){
            return dao.save(vo);
        }
        return null;
    }

    public VideoComment delete(int id){
        VideoComment target = dao.findById(id).orElse(null);
        dao.delete(target);
        return target;
    }

    // 영상 1개에 따른 댓글 목록
    public List<VideoComment> findByVideoCode(int code){
        return dao.findByVideoCode(code);
    }
}

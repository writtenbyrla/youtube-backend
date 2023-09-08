package com.kh.youtube.service;

import com.kh.youtube.domain.Channel;
import com.kh.youtube.domain.Member;
import com.kh.youtube.repo.ChannelDAO;
import com.kh.youtube.repo.MemberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelDAO channelDAO;

    @Autowired
    private MemberDAO memberDAO;

    public List<Channel> showAll(){
        return channelDAO.findAll();
    }

    public Channel show(int id){
        Channel channel = channelDAO.findById(id).orElse(null); // 채널에 대한 정보만
        Member member =  memberDAO.findById(channel.getMember().getId()).orElse(null); // channel의 member(getId)를 통해 멤버 정보 받아와서 member에 담기
        channel.setMember(member);
        return channel;
    }

    // 특정 멤버의 모든 채널 조회
    public List<Channel> showMember(String id){
        return channelDAO.findByMemberId(id);
    }

    public Channel create(Channel channel){
        return channelDAO.save(channel);
    }

    public Channel update(Channel channel){

        Channel target = channelDAO.findById(channel.getChannelCode()).orElse(null);

        if(target!=null){
            return channelDAO.save(channel);
        }
        return null;

    }

    public Channel delete(int code){
        Channel target = channelDAO.findById(code).orElse(null);
        channelDAO.delete(target);
        return target;
    }
}

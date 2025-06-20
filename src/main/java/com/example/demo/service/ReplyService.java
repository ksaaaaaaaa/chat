package com.example.demo.service;

import com.example.demo.entity.Reply;
import com.example.demo.dto.LikeDTO;
import java.util.List;

public interface ReplyService {
    Reply createReply(Reply reply);
    void deleteReply(Integer id, Integer userId, String userRole);
    void deleteByTopicId(Integer topicId);
    List<Reply> getRepliesByTopicId(Integer topicId);
    Reply findById(Integer id);
    void toggleLike(Integer replyId, Integer userId);
    List<LikeDTO> getUserLikes(Integer userId);
    boolean checkLike(Integer replyId, Integer userId);
    Reply updateReply(Integer id, String content, Integer userId);
} 
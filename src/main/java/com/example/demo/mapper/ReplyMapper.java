package com.example.demo.mapper;

import com.example.demo.entity.Reply;
import com.example.demo.dto.LikeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReplyMapper {
    void insert(Reply reply);
    List<Reply> findByTopicId(Integer topicId);
    Reply findById(Integer id);
    void updateLikesCount(@Param("id") Integer id, @Param("likesCount") Integer likesCount);
    boolean existsLike(@Param("userId") Integer userId, @Param("replyId") Integer replyId);
    void insertLike(@Param("userId") Integer userId, @Param("replyId") Integer replyId);
    void deleteLike(@Param("userId") Integer userId, @Param("replyId") Integer replyId);
    void deleteReply(Integer id);
    void deleteByTopicId(Integer topicId);
    List<LikeDTO> findUserLikes(Integer userId);
    void update(Reply reply);
} 
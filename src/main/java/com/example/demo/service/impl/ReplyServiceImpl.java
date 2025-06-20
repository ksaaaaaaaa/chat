package com.example.demo.service.impl;

import com.example.demo.entity.Reply;
import com.example.demo.dto.LikeDTO;
import com.example.demo.mapper.ReplyMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    private static final Logger logger = LoggerFactory.getLogger(ReplyServiceImpl.class);

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Reply createReply(Reply reply) {
        replyMapper.insert(reply);
        return replyMapper.findById(reply.getId());
    }

    @Override
    public List<Reply> getRepliesByTopicId(Integer topicId) {
        return replyMapper.findByTopicId(topicId);
    }

    @Override
    @Transactional
    public void toggleLike(Integer replyId, Integer userId) {
        try {
            Reply reply = replyMapper.findById(replyId);
            if (reply == null) {
                throw new RuntimeException("回复不存在");
            }

            boolean hasLiked = replyMapper.existsLike(userId, replyId);
            if (hasLiked) {
                // 取消点赞
                replyMapper.updateLikesCount(replyId, reply.getLikesCount() - 1);
                replyMapper.deleteLike(userId, replyId);
                
                // 更新被点赞用户的获赞数
                User user = userMapper.findById(reply.getUserId());
                userMapper.updateLikesCount(reply.getUserId(), user.getLikesCount() - 1);
                
                logger.info("用户 {} 取消点赞回复 {}", userId, replyId);
            } else {
                // 添加点赞
                replyMapper.updateLikesCount(replyId, reply.getLikesCount() + 1);
                replyMapper.insertLike(userId, replyId);
                
                // 更新被点赞用户的获赞数
                User user = userMapper.findById(reply.getUserId());
                userMapper.updateLikesCount(reply.getUserId(), user.getLikesCount() + 1);
                
                logger.info("用户 {} 点赞回复 {}", userId, replyId);
            }
        } catch (Exception e) {
            logger.error("处理点赞失败, userId: {}, replyId: {}", userId, replyId, e);
            throw new RuntimeException("处理点赞失败", e);
        }
    }

    @Override
    public boolean checkLike(Integer replyId, Integer userId) {
        return replyMapper.existsLike(userId, replyId);
    }

    @Override
    public Reply findById(Integer id) {
        return replyMapper.findById(id);
    }

    @Override
    @Transactional
    public void deleteReply(Integer replyId, Integer userId, String userRole) {
        Reply reply = replyMapper.findById(replyId);
        if (reply == null) {
            throw new RuntimeException("回复不存在");
        }
        if (!reply.getUserId().equals(userId) && !"admin".equals(userRole)) {
            throw new RuntimeException("无权删除此回复");
        }
        replyMapper.deleteReply(replyId);
    }

    @Override
    @Transactional
    public void deleteByTopicId(Integer topicId) {
        replyMapper.deleteByTopicId(topicId);
    }

    @Override
    public List<LikeDTO> getUserLikes(Integer userId) {
        try {
            logger.info("获取用户点赞列表, userId: {}", userId);
            List<LikeDTO> likes = replyMapper.findUserLikes(userId);
            logger.info("获取到 {} 条点赞记录", likes.size());
            return likes;
        } catch (Exception e) {
            logger.error("获取用户点赞列表失败, userId: {}", userId, e);
            throw new RuntimeException("获取点赞列表失败", e);
        }
    }

    @Override
    @Transactional
    public Reply updateReply(Integer id, String content, Integer userId) {
        Reply reply = replyMapper.findById(id);
        if (reply == null) {
            throw new RuntimeException("回复不存在");
        }
        if (!reply.getUserId().equals(userId)) {
            throw new RuntimeException("无权编辑此回复");
        }
        
        reply.setContent(content);
        replyMapper.update(reply);
        return replyMapper.findById(id);
    }
} 
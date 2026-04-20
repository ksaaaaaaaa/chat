package com.example.demo.service.impl;

import com.example.demo.dto.TopicDTO;
import com.example.demo.dto.FavoriteDTO;
import com.example.demo.entity.Topic;
import com.example.demo.entity.User;
import com.example.demo.mapper.TopicMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.TopicService;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TopicServiceImpl implements TopicService {
    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReplyService replyService;

    // 预设的话题分类
    private static final List<String> DEFAULT_CATEGORIES = Arrays.asList(
        "生活日常",
        "美食分享",
        "旅行游记",
        "电影音乐",
        "读书交流",
        "科技数码",
        "游戏动漫",
        "体育运动",
        "职场经验",
        "教育学习",
        "时尚穿搭",
        "健康养生",
        "情感交流"
    );

    @Override
    public Topic createTopic(Topic topic) {
        // 验证分类是否有效
        if (!DEFAULT_CATEGORIES.contains(topic.getCategory())) {
            throw new RuntimeException("无效的话题分类");
        }
        topicMapper.insert(topic);
        return topic;
    }

    @Override
    public Topic updateTopic(Integer id, Topic topic) {
        // 验证分类是否有效
        if (!DEFAULT_CATEGORIES.contains(topic.getCategory())) {
            throw new RuntimeException("无效的话题分类");
        }
        
        Topic existingTopic = topicMapper.findById(id);
        if (existingTopic == null) {
            throw new RuntimeException("话题不存在");
        }
        if (!existingTopic.getUserId().equals(topic.getUserId())) {
            throw new RuntimeException("无权修改此话题");
        }
        topic.setId(id);
        topicMapper.update(topic);
        return topicMapper.findById(id);
    }

    @Override
    @Transactional
    public void deleteTopic(Integer id, Integer userId, String userRole) {
        Topic topic = topicMapper.findById(id);
        if (topic == null) {
            throw new RuntimeException("话题不存在");
        }
        if (!topic.getUserId().equals(userId) && !"admin".equals(userRole)) {
            throw new RuntimeException("无权删除此话题");
        }
        // 先删除话题的所有回复
        replyService.deleteByTopicId(id);
        // 再删除话题
        topicMapper.delete(id);
    }

    @Override
    public Topic getTopic(Integer id) {
        return topicMapper.findById(id);
    }

    @Override
    public List<Topic> searchTopics(TopicDTO searchDTO) {
        return topicMapper.search(
            searchDTO.getTitle(),
            searchDTO.getCategory(),
            searchDTO.getStartTime(),
            searchDTO.getEndTime()
        );
    }

    @Override
    public List<String> getAllCategories() {
        return DEFAULT_CATEGORIES;
    }

    @Override
    @Transactional
    public void addFavorite(Integer topicId, Integer userId) {
        try {
            if (!topicMapper.existsFavorite(userId, topicId)) {
                // 更新话题收藏数
                Topic topic = topicMapper.findById(topicId);
                topicMapper.updateFavoritesCount(topicId, topic.getFavoritesCount() + 1);
                
                // 更新用户收藏数
                User user = userMapper.findById(userId);
                userMapper.updateFavoritesCount(userId, user.getFavoritesCount() + 1);
                
                // 添加收藏记录
                topicMapper.insertFavorite(userId, topicId);
                
                logger.info("用户 {} 成功收藏话题 {}", userId, topicId);
            }
        } catch (Exception e) {
            logger.error("可能出现异常了");
            logger.error(null);
            logger.error("添加收藏失败, userId: {}, topicId: {}", userId, topicId, e);
            throw new RuntimeException("添加收藏失败", e);
        }
    }

    @Override
    @Transactional
    public void removeFavorite(Integer topicId, Integer userId) {
        try {
            if (topicMapper.existsFavorite(userId, topicId)) {
                // 更新话题收藏数
                Topic topic = topicMapper.findById(topicId);
                topicMapper.updateFavoritesCount(topicId, topic.getFavoritesCount() - 1);
                
                // 更新用户收藏数
                User user = userMapper.findById(userId);
                userMapper.updateFavoritesCount(userId, user.getFavoritesCount() - 1);
                
                // 删除收藏记录
                topicMapper.deleteFavorite(userId, topicId);
                
                logger.info("用户 {} 成功取消收藏话题 {}", userId, topicId);
            }
        } catch (Exception e) {
            logger.error("取消收藏失败, userId: {}, topicId: {}", userId, topicId, e);
            throw new RuntimeException("取消收藏失败", e);
        }
    }

    @Override
    public boolean checkFavorite(Integer topicId, Integer userId) {
        return topicMapper.existsFavorite(userId, topicId);
    }

    @Override
    @Transactional
    public void incrementViewCount(Integer topicId) {
        Topic topic = topicMapper.findById(topicId);
        if (topic != null) {
            topicMapper.updateViewCount(topicId, topic.getViewCount() + 1);
        }
    }

    @Override
    @Transactional
    public void incrementReplyCount(Integer topicId) {
        Topic topic = topicMapper.findById(topicId);
        if (topic != null) {
            topicMapper.updateReplyCount(topicId, topic.getReplyCount() + 1);
        }
    }

    @Override
    @Transactional
    public void updateReplyCount(Integer topicId, Integer replyCount) {
        topicMapper.updateReplyCount(topicId, replyCount);
    }

    @Override
    public Topic findById(Integer id) {
        return topicMapper.findById(id);
    }

    @Override
    public List<FavoriteDTO> getUserFavorites(Integer userId) {
        return topicMapper.findUserFavorites(userId);
    }

    @Override
    public List<Topic> findAllWithFilters(Map<String, Object> filters, int page, int size) {
        int offset = (page - 1) * size;
        return topicMapper.findAllWithFilters(filters, offset, size);
    }

    @Override
    public long countWithFilters(Map<String, Object> filters) {
        return topicMapper.countWithFilters(filters);
    }

    @Override
    @Transactional
    public Topic toggleStatus(Integer id) {
        Topic topic = topicMapper.findByIdForAdmin(id);
        if (topic == null) {
            throw new RuntimeException("话题不存在");
        }
        
        String newStatus = topic.getStatus().equals("normal") ? "deleted" : "normal";
        topic.setStatus(newStatus);
        topicMapper.updateStatus(topic);
        
        return topic;
    }
} 
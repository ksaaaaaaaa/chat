package com.example.demo.service;

import com.example.demo.dto.TopicDTO;
import com.example.demo.dto.FavoriteDTO;
import com.example.demo.entity.Topic;
import java.util.List;
import java.util.Map;

public interface TopicService {
    Topic createTopic(Topic topic);
    Topic updateTopic(Integer id, Topic topic);
    void deleteTopic(Integer id, Integer userId, String userRole);
    Topic getTopic(Integer id);
    List<Topic> searchTopics(TopicDTO searchDTO);
    List<String> getAllCategories();
    void addFavorite(Integer topicId, Integer userId);
    void removeFavorite(Integer topicId, Integer userId);
    boolean checkFavorite(Integer topicId, Integer userId);
    void incrementViewCount(Integer topicId);
    Topic findById(Integer id);
    void incrementReplyCount(Integer topicId);
    void updateReplyCount(Integer topicId, Integer replyCount);
    List<FavoriteDTO> getUserFavorites(Integer userId);
    List<Topic> findAllWithFilters(Map<String, Object> filters, int page, int size);
    long countWithFilters(Map<String, Object> filters);
    Topic toggleStatus(Integer id);
} 
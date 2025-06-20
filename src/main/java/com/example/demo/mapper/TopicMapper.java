package com.example.demo.mapper;

import com.example.demo.entity.Topic;
import com.example.demo.dto.FavoriteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface TopicMapper {
    void insert(Topic topic);
    void update(Topic topic);
    void delete(Integer id);
    Topic findById(Integer id);
    List<Topic> search(@Param("title") String title,
                      @Param("category") String category,
                      @Param("startTime") LocalDateTime startTime,
                      @Param("endTime") LocalDateTime endTime);
    List<String> getAllCategories();
    boolean existsFavorite(@Param("userId") Integer userId, @Param("topicId") Integer topicId);
    void updateFavoritesCount(@Param("id") Integer id, @Param("favoritesCount") Integer favoritesCount);
    void insertFavorite(@Param("userId") Integer userId, @Param("topicId") Integer topicId);
    void deleteFavorite(@Param("userId") Integer userId, @Param("topicId") Integer topicId);
    void updateViewCount(@Param("id") Integer id, @Param("viewCount") Integer viewCount);
    void updateReplyCount(@Param("id") Integer id, @Param("replyCount") Integer replyCount);
    List<FavoriteDTO> findUserFavorites(Integer userId);
    List<Topic> findAllWithFilters(@Param("filters") Map<String, Object> filters,
                                  @Param("offset") int offset,
                                  @Param("size") int size);
    long countWithFilters(@Param("filters") Map<String, Object> filters);
    Topic findByIdForAdmin(Integer id);
    void updateStatus(Topic topic);
} 
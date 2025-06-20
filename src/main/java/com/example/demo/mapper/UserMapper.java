package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    void insert(User user);
    User findById(Integer id);
    User findByUsername(String username);
    User findByEmail(String email);
    void update(User user);
    void updateFavoritesCount(@Param("id") Integer id, @Param("favoritesCount") Integer favoritesCount);
    void updateLikesCount(@Param("id") Integer id, @Param("likesCount") Integer likesCount);
    
    // 管理员功能
    List<User> findAllWithFilters(@Param("filters") Map<String, Object> filters, 
                                 @Param("offset") int offset, 
                                 @Param("size") int size);
    long countWithFilters(@Param("filters") Map<String, Object> filters);
} 
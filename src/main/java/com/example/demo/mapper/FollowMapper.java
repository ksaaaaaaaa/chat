package com.example.demo.mapper;

import com.example.demo.dto.FollowDTO;
import com.example.demo.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {
    void insert(Follow follow);
    void delete(@Param("followerId") Integer followerId, @Param("followingId") Integer followingId);
    boolean exists(@Param("followerId") Integer followerId, @Param("followingId") Integer followingId);
    
    List<FollowDTO> findFollowingByUserId(Integer userId);
    List<FollowDTO> findFollowersByUserId(Integer userId);
    
    int countFollowing(Integer userId);
    int countFollowers(Integer userId);
} 

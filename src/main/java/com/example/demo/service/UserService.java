package com.example.demo.service;

import com.example.demo.dto.UserLoginDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.entity.User;

import java.util.List;
import java.util.Map;
 
public interface UserService {
    User register(UserRegisterDTO registerDTO);
    User login(UserLoginDTO loginDTO);
    User updateUser(Integer userId, UserUpdateDTO updateDTO);
    void deleteUser(Integer userId);
    User findById(Integer userId);
    User findByUsername(String username);
    User toggleUserStatus(Integer userId);
    
    // 管理员功能
    List<User> findAllWithFilters(Map<String, Object> filters, int page, int size);
    long countWithFilters(Map<String, Object> filters);
    User adminUpdateUser(Integer userId, User user);
} 
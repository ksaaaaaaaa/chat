package com.example.demo.service.impl;

import com.example.demo.dto.UserLoginDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserMapper userMapper;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(registerDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userMapper.findByEmail(registerDTO.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setStatus("active");
        
        // 验证码为412时设置为管理员
        if ("412".equals(registerDTO.getVerificationCode())) {
            user.setRole("admin");
        } else {
            user.setRole("user");
        }

        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(UserLoginDTO loginDTO) {
        User user = userMapper.findByUsername(loginDTO.getUsername());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 检查用户状态
        if ("banned".equals(user.getStatus())) {
            throw new RuntimeException("您的账号已被封禁");
        }
        return user;
    }

    @Override
    public User findById(Integer userId) {
        return userMapper.findById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    @Transactional
    public User updateUser(Integer userId, UserUpdateDTO updateDTO) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证当前密码
        if (updateDTO.getCurrentPassword() != null && !updateDTO.getCurrentPassword().isEmpty()) {
            if (!passwordEncoder.matches(updateDTO.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("当前密码错误");
            }
        }

        // 检查新用户名是否已存在
        if (!updateDTO.getUsername().equals(user.getUsername())) {
            User existingUser = userMapper.findByUsername(updateDTO.getUsername());
            if (existingUser != null) {
                throw new RuntimeException("用户名已存在");
            }
        }

        // 检查新邮箱是否已存在
        if (!updateDTO.getEmail().equals(user.getEmail())) {
            User existingUser = userMapper.findByEmail(updateDTO.getEmail());
            if (existingUser != null) {
                throw new RuntimeException("邮箱已被注册");
            }
        }

        // 更新用户信息
        user.setUsername(updateDTO.getUsername());
        user.setEmail(updateDTO.getEmail());

        // 如果提供了新密码，则更新密码
        if (updateDTO.getNewPassword() != null && !updateDTO.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDTO.getNewPassword()));
        }

        userMapper.update(user);
        logger.info("用户信息更新成功, userId: {}", userId);
        
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 将用户状态设置为已禁用
        user.setStatus("banned");
        userMapper.update(user);
        logger.info("用户账号已禁用, userId: {}", userId);
    }

    @Override
    @Transactional
    public User toggleUserStatus(Integer userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 切换用户状态
        user.setStatus("banned".equals(user.getStatus()) ? "active" : "banned");
        userMapper.update(user);
        logger.info("用户状态已更新为: {}, userId: {}", user.getStatus(), userId);
        
        return user;
    }

    @Override
    public List<User> searchUsers(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return userMapper.searchUsers(keyword, offset, size);
    }

    @Override
    public long countSearchUsers(String keyword) {
        return userMapper.countSearchUsers(keyword);
    }

    // 管理员功能实现
    @Override
    public List<User> findAllWithFilters(Map<String, Object> filters, int page, int size) {
        int offset = (page - 1) * size;
        return userMapper.findAllWithFilters(filters, offset, size);
    }

    @Override
    public long countWithFilters(Map<String, Object> filters) {
        return userMapper.countWithFilters(filters);
    }

    @Override
    public User adminUpdateUser(Integer userId, User updateUser) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查新用户名是否已存在
        if (!updateUser.getUsername().equals(user.getUsername())) {
            User existingUser = userMapper.findByUsername(updateUser.getUsername());
            if (existingUser != null) {
                throw new RuntimeException("用户名已存在");
            }
        }

        // 检查新邮箱是否已存在
        if (!updateUser.getEmail().equals(user.getEmail())) {
            User existingUser = userMapper.findByEmail(updateUser.getEmail());
            if (existingUser != null) {
                throw new RuntimeException("邮箱已被注册");
            }
        }

        // 更新用户信息
        user.setUsername(updateUser.getUsername());
        user.setEmail(updateUser.getEmail());
        user.setRole(updateUser.getRole());

        userMapper.update(user);
        logger.info("管理员更新用户信息成功, userId: {}", userId);
        
        return user;
    }
} 
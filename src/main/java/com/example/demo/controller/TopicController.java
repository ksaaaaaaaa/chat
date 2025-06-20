package com.example.demo.controller;

import com.example.demo.dto.FavoriteDTO;
import com.example.demo.dto.TopicDTO;
import com.example.demo.entity.Topic;
import com.example.demo.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true", allowedHeaders = "*")
public class TopicController {
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<?> createTopic(@RequestBody Topic topic) {
        try {
            Topic newTopic = topicService.createTopic(topic);
            return ResponseEntity.ok(newTopic);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTopic(@PathVariable Integer id, @RequestBody Topic topic) {
        try {
            Topic updatedTopic = topicService.updateTopic(id, topic);
            return ResponseEntity.ok(updatedTopic);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(
            @PathVariable Integer id,
            @RequestParam Integer userId,
            @RequestParam String userRole) {
        try {
            topicService.deleteTopic(id, userId, userRole);
            Map<String, String> response = new HashMap<>();
            response.put("message", "话题删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Integer id) {
        Topic topic = topicService.findById(id);
        if (topic != null) {
            topicService.incrementViewCount(id);
            topic = topicService.findById(id);
            return ResponseEntity.ok(topic);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTopics(TopicDTO searchDTO) {
        try {
            List<Topic> topics = topicService.searchTopics(searchDTO);
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<String> categories = topicService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<?> addFavorite(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> request) {
        try {
            logger.info("收到添加收藏请求, topicId: {}, userId: {}", id, request.get("userId"));
            topicService.addFavorite(id, request.get("userId"));
            Topic topic = topicService.findById(id);
            logger.info("收藏成功, topicId: {}, userId: {}", id, request.get("userId"));
            return ResponseEntity.ok(Map.of(
                "message", "收藏成功",
                "topic", topic
            ));
        } catch (Exception e) {
            logger.error("添加收藏失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<?> removeFavorite(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> request) {
        try {
            logger.info("收到取消收藏请求, topicId: {}, userId: {}", id, request.get("userId"));
            topicService.removeFavorite(id, request.get("userId"));
            Topic topic = topicService.findById(id);
            logger.info("取消收藏成功, topicId: {}, userId: {}", id, request.get("userId"));
            return ResponseEntity.ok(Map.of(
                "message", "取消收藏成功",
                "topic", topic
            ));
        } catch (Exception e) {
            logger.error("取消收藏失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/favorite/check")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @PathVariable Integer id,
            @RequestParam Integer userId) {
        boolean favorited = topicService.checkFavorite(id, userId);
        return ResponseEntity.ok(Map.of("favorited", favorited));
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<Topic> getTopicInfo(@PathVariable Integer id) {
        Topic topic = topicService.findById(id);
        if (topic != null) {
            return ResponseEntity.ok(topic);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/favorites")
    public ResponseEntity<List<FavoriteDTO>> getUserFavorites(@PathVariable Integer userId) {
        try {
            logger.info("接收到获取用户收藏列表请求, userId: {}", userId);
            List<FavoriteDTO> favorites = topicService.getUserFavorites(userId);
            logger.info("成功获取到用户收藏列表, userId: {}, count: {}", userId, favorites.size());
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            logger.error("获取用户收藏列表失败, userId: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }
} 
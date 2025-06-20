package com.example.demo.controller;

import com.example.demo.entity.Reply;
import com.example.demo.entity.Topic;
import com.example.demo.dto.LikeDTO;
import com.example.demo.service.ReplyService;
import com.example.demo.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true", allowedHeaders = "*")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private TopicService topicService;

    @PostMapping("/replies")
    public ResponseEntity<Map<String, Object>> createReply(@RequestBody Reply reply) {
        Reply createdReply = replyService.createReply(reply);
        // 更新话题回复数
        topicService.incrementReplyCount(reply.getTopicId());
        // 获取更新后的话题数据
        Topic updatedTopic = topicService.findById(reply.getTopicId());
        
        return ResponseEntity.ok(Map.of(
            "reply", createdReply,
            "topic", updatedTopic
        ));
    }

    @GetMapping("/topics/{topicId}/replies")
    public ResponseEntity<List<Reply>> getRepliesByTopicId(@PathVariable Integer topicId) {
        List<Reply> replies = replyService.getRepliesByTopicId(topicId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping("/replies/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> request) {
        Integer userId = request.get("userId");
        boolean isLiked = replyService.checkLike(id, userId);
        
        replyService.toggleLike(id, userId);
        
        Reply reply = replyService.findById(id);
        return ResponseEntity.ok(Map.of(
            "likesCount", reply.getLikesCount(),
            "isLiked", !isLiked
        ));
    }

    @DeleteMapping("/replies/{id}")
    public ResponseEntity<?> deleteReply(
            @PathVariable Integer id,
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "user") String userRole) {
        try {
            // 先获取回复信息
            Reply reply = replyService.findById(id);
            if (reply == null) {
                return ResponseEntity.notFound().build();
            }

            // 获取话题信息
            Topic topic = topicService.findById(reply.getTopicId());
            if (topic == null) {
                return ResponseEntity.notFound().build();
            }

            // 删除回复
            replyService.deleteReply(id, userId, userRole);

            // 更新话题的回复数
            topicService.updateReplyCount(topic.getId(), topic.getReplyCount() - 1);

            // 返回更新后的话题信息
            Topic updatedTopic = topicService.findById(topic.getId());
            return ResponseEntity.ok(Map.of("topic", updatedTopic));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users/{userId}/likes")
    public ResponseEntity<List<LikeDTO>> getUserLikes(@PathVariable Integer userId) {
        try {
            List<LikeDTO> likes = replyService.getUserLikes(userId);
            return ResponseEntity.ok(likes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/replies/{id}")
    public ResponseEntity<?> updateReply(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        try {
            String content = (String) request.get("content");
            Integer userId = (Integer) request.get("userId");
            
            Reply updatedReply = replyService.updateReply(id, content, userId);
            return ResponseEntity.ok(updatedReply);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 
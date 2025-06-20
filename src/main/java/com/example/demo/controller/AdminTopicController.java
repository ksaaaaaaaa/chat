package com.example.demo.controller;

import com.example.demo.entity.Topic;
import com.example.demo.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/topics")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public ResponseEntity<?> getTopics(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status
    ) {
        try {
            Map<String, Object> filters = new HashMap<>();
            if (title != null) filters.put("title", title);
            if (category != null) filters.put("category", category);
            if (status != null) filters.put("status", status);

            List<Topic> topics = topicService.findAllWithFilters(filters, page, size);
            long total = topicService.countWithFilters(filters);

            Map<String, Object> response = new HashMap<>();
            response.put("topics", topics);
            response.put("total", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleTopicStatus(@PathVariable Integer id) {
        try {
            Topic topic = topicService.toggleStatus(id);
            String message = topic.getStatus().equals("normal") ? "话题已恢复" : "话题已删除";
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 
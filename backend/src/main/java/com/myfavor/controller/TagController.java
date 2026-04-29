package com.myfavor.controller;

import com.myfavor.dto.ApiResponse;
import com.myfavor.entity.Tag;
import com.myfavor.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getTagsByUserId(@PathVariable Long userId) {
        List<Tag> tags = tagService.getTagsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

    @GetMapping
    public ResponseEntity<?> getMyTags() {
        List<Tag> tags = tagService.getMyTags();
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag request) {
        try {
            Tag tag = tagService.createTag(request.getName());
            return ResponseEntity.ok(ApiResponse.success(tag));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<?> createTags(@RequestBody List<String> tagNames) {
        try {
            List<Tag> tags = tagService.createTagsIfNotExist(tagNames);
            return ResponseEntity.ok(ApiResponse.success(tags));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @RequestBody Tag request) {
        try {
            Tag tag = tagService.updateTag(id, request.getName());
            return ResponseEntity.ok(ApiResponse.success(tag));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        try {
            tagService.deleteTag(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

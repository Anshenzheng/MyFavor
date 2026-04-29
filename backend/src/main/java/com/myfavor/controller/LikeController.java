package com.myfavor.controller;

import com.myfavor.dto.ApiResponse;
import com.myfavor.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/{figureId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long figureId) {
        try {
            boolean isLiked = likeService.toggleLike(figureId);
            return ResponseEntity.ok(ApiResponse.success(isLiked ? "点赞成功" : "取消点赞成功", isLiked));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{figureId}")
    public ResponseEntity<?> checkLike(@PathVariable Long figureId) {
        boolean isLiked = likeService.isLiked(figureId);
        long count = likeService.getLikeCount(figureId);
        return ResponseEntity.ok(ApiResponse.success(new LikeStatus(isLiked, count)));
    }

    public record LikeStatus(boolean liked, long count) {}
}

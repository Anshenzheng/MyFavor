package com.myfavor.controller;

import com.myfavor.dto.ApiResponse;
import com.myfavor.service.WantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wants")
@CrossOrigin(origins = "*")
public class WantController {

    @Autowired
    private WantService wantService;

    @PostMapping("/{figureId}")
    public ResponseEntity<?> toggleWant(@PathVariable Long figureId) {
        try {
            boolean isWanted = wantService.toggleWant(figureId);
            return ResponseEntity.ok(ApiResponse.success(isWanted ? "标记想要成功" : "取消标记想要成功", isWanted));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{figureId}")
    public ResponseEntity<?> checkWant(@PathVariable Long figureId) {
        boolean isWanted = wantService.isWanted(figureId);
        long count = wantService.getWantCount(figureId);
        return ResponseEntity.ok(ApiResponse.success(new WantStatus(isWanted, count)));
    }

    public record WantStatus(boolean wanted, long count) {}
}

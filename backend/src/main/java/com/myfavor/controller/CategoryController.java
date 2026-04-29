package com.myfavor.controller;

import com.myfavor.dto.ApiResponse;
import com.myfavor.entity.Category;
import com.myfavor.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCategoriesByUserId(@PathVariable Long userId) {
        List<Category> categories = categoryService.getCategoriesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping
    public ResponseEntity<?> getMyCategories() {
        List<Category> categories = categoryService.getMyCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category request) {
        try {
            Category category = categoryService.createCategory(request.getName());
            return ResponseEntity.ok(ApiResponse.success(category));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category request) {
        try {
            Category category = categoryService.updateCategory(id, request.getName());
            return ResponseEntity.ok(ApiResponse.success(category));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

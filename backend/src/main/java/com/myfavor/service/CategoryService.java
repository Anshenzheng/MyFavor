package com.myfavor.service;

import com.myfavor.entity.Category;
import com.myfavor.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public List<Category> getCategoriesByUserId(Long userId) {
        return categoryRepository.findByUserIdOrderByName(userId);
    }

    public List<Category> getMyCategories() {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            return List.of();
        }
        return categoryRepository.findByUserIdOrderByName(userId);
    }

    @Transactional
    public Category createCategory(String name) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Category category = new Category();
        category.setName(name);
        category.setUserId(userId);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, String name) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("分类不存在"));

        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此分类");
        }

        category.setName(name);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("分类不存在"));

        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此分类");
        }

        categoryRepository.deleteById(id);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}

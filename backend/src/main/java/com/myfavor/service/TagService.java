package com.myfavor.service;

import com.myfavor.entity.Tag;
import com.myfavor.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserService userService;

    public List<Tag> getTagsByUserId(Long userId) {
        return tagRepository.findByUserIdOrderByName(userId);
    }

    public List<Tag> getMyTags() {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            return List.of();
        }
        return tagRepository.findByUserIdOrderByName(userId);
    }

    @Transactional
    public Tag createTag(String name) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        if (tagRepository.existsByUserIdAndName(userId, name)) {
            return tagRepository.findByUserIdAndName(userId, name).orElse(null);
        }

        Tag tag = new Tag();
        tag.setName(name);
        tag.setUserId(userId);

        return tagRepository.save(tag);
    }

    @Transactional
    public List<Tag> createTagsIfNotExist(List<String> tagNames) {
        Long userId = userService.getCurrentUserId();
        if (userId == null || tagNames == null || tagNames.isEmpty()) {
            return List.of();
        }

        List<Tag> result = new ArrayList<>();
        for (String name : tagNames) {
            if (name != null && !name.trim().isEmpty()) {
                String trimmedName = name.trim();
                Tag tag = tagRepository.findByUserIdAndName(userId, trimmedName).orElse(null);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(trimmedName);
                    tag.setUserId(userId);
                    tag = tagRepository.save(tag);
                }
                result.add(tag);
            }
        }
        return result;
    }

    @Transactional
    public Tag updateTag(Long id, String name) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("标签不存在"));

        if (!tag.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此标签");
        }

        if (tagRepository.existsByUserIdAndName(userId, name)) {
            throw new RuntimeException("标签名称已存在");
        }

        tag.setName(name);
        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("标签不存在"));

        if (!tag.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此标签");
        }

        tagRepository.deleteById(id);
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    public List<Tag> findByIds(List<Long> ids) {
        return tagRepository.findAllById(ids);
    }
}

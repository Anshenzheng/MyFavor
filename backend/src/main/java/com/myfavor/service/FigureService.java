package com.myfavor.service;

import com.myfavor.dto.FigureCreateRequest;
import com.myfavor.dto.FigureDTO;
import com.myfavor.dto.FigureUpdateRequest;
import com.myfavor.entity.*;
import com.myfavor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FigureService {
    @Autowired
    private FigureRepository figureRepository;

    @Autowired
    private FigureImageRepository figureImageRepository;

    @Autowired
    private FigureTagRepository figureTagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private WantRepository wantRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    public Page<FigureDTO> getPublicFigures(Pageable pageable) {
        Page<Figure> figures = figureRepository.findByIsPublicTrue(pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> getMyFigures(Pageable pageable) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            return Page.empty(pageable);
        }
        Page<Figure> figures = figureRepository.findByUserId(userId, pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> getUserFigures(Long userId, Pageable pageable) {
        Page<Figure> figures = figureRepository.findByUserIdAndIsPublic(userId, true, pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> getPopularFigures(Pageable pageable) {
        Page<Figure> figures = figureRepository.findPopularFigures(pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> getMostViewedFigures(Pageable pageable) {
        Page<Figure> figures = figureRepository.findMostViewedFigures(pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> searchPublicFigures(String keyword, Pageable pageable) {
        Page<Figure> figures = figureRepository.searchPublicFigures(keyword, pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> searchMyFigures(String keyword, Pageable pageable) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            return Page.empty(pageable);
        }
        Page<Figure> figures = figureRepository.searchUserFigures(userId, keyword, pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> getFiguresByCategory(Long categoryId, Pageable pageable) {
        Page<Figure> figures = figureRepository.findByCategoryIdAndIsPublicTrue(categoryId, pageable);
        return figures.map(this::convertToDTO);
    }

    public Page<FigureDTO> getFiguresByTag(Long tagId, Pageable pageable) {
        Page<Figure> figures = figureRepository.findByTagIdAndIsPublicTrue(tagId, pageable);
        return figures.map(this::convertToDTO);
    }

    public FigureDTO getFigureById(Long id) {
        Figure figure = figureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("手办不存在"));

        if (!figure.getIsPublic()) {
            Long currentUserId = userService.getCurrentUserId();
            if (currentUserId == null || !currentUserId.equals(figure.getUserId())) {
                throw new RuntimeException("无权限查看此手办");
            }
        }

        figure.setViewCount(figure.getViewCount() + 1);
        figureRepository.save(figure);

        return convertToDTO(figure);
    }

    @Transactional
    public FigureDTO createFigure(FigureCreateRequest request) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Figure figure = new Figure();
        figure.setName(request.getName());
        figure.setDescription(request.getDescription());
        figure.setPrice(request.getPrice());
        figure.setPurchaseDate(request.getPurchaseDate());
        figure.setUserId(userId);
        figure.setCategoryId(request.getCategoryId());
        figure.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);

        Figure savedFigure = figureRepository.save(figure);

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                FigureImage image = new FigureImage();
                image.setFigureId(savedFigure.getId());
                image.setImageUrl(request.getImageUrls().get(i));
                image.setIsPrimary(i == 0);
                image.setSortOrder(i);
                figureImageRepository.save(image);
            }
        }

        List<Tag> tags = processTags(request.getTagNames(), request.getTagIds(), userId);
        for (Tag tag : tags) {
            FigureTag figureTag = new FigureTag();
            figureTag.setFigureId(savedFigure.getId());
            figureTag.setTagId(tag.getId());
            figureTagRepository.save(figureTag);
        }

        return convertToDTO(savedFigure);
    }

    @Transactional
    public FigureDTO updateFigure(Long id, FigureUpdateRequest request) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Figure figure = figureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("手办不存在"));

        if (!figure.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此手办");
        }

        figure.setName(request.getName());
        figure.setDescription(request.getDescription());
        figure.setPrice(request.getPrice());
        figure.setPurchaseDate(request.getPurchaseDate());
        figure.setCategoryId(request.getCategoryId());
        if (request.getIsPublic() != null) {
            figure.setIsPublic(request.getIsPublic());
        }

        Figure savedFigure = figureRepository.save(figure);

        if (request.getImageUrls() != null) {
            figureImageRepository.deleteByFigureId(id);
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                FigureImage image = new FigureImage();
                image.setFigureId(id);
                image.setImageUrl(request.getImageUrls().get(i));
                if (request.getPrimaryImageUrl() != null) {
                    image.setIsPrimary(request.getPrimaryImageUrl().equals(request.getImageUrls().get(i)));
                } else {
                    image.setIsPrimary(i == 0);
                }
                image.setSortOrder(i);
                figureImageRepository.save(image);
            }
        }

        figureTagRepository.deleteByFigureId(id);
        List<Tag> tags = processTags(request.getTagNames(), request.getTagIds(), userId);
        for (Tag tag : tags) {
            if (!figureTagRepository.existsByFigureIdAndTagId(id, tag.getId())) {
                FigureTag figureTag = new FigureTag();
                figureTag.setFigureId(id);
                figureTag.setTagId(tag.getId());
                figureTagRepository.save(figureTag);
            }
        }

        return convertToDTO(savedFigure);
    }

    @Transactional
    public void deleteFigure(Long id) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Figure figure = figureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("手办不存在"));

        if (!figure.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此手办");
        }

        figureImageRepository.deleteByFigureId(id);
        figureTagRepository.deleteByFigureId(id);
        likeRepository.deleteByFigureId(id);
        wantRepository.deleteByFigureId(id);
        figureRepository.deleteById(id);
    }

    private List<Tag> processTags(List<String> tagNames, List<Long> tagIds, Long userId) {
        List<Tag> tags = tagService.createTagsIfNotExist(tagNames);
        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> existingTags = tagRepository.findAllById(tagIds);
            for (Tag tag : existingTags) {
                if (tag.getUserId().equals(userId)) {
                    if (tags.stream().noneMatch(t -> t.getId().equals(tag.getId()))) {
                        tags.add(tag);
                    }
                }
            }
        }
        return tags;
    }

    private FigureDTO convertToDTO(Figure figure) {
        FigureDTO dto = new FigureDTO();
        dto.setId(figure.getId());
        dto.setName(figure.getName());
        dto.setDescription(figure.getDescription());
        dto.setPrice(figure.getPrice());
        dto.setPurchaseDate(figure.getPurchaseDate());
        dto.setUserId(figure.getUserId());
        dto.setCategoryId(figure.getCategoryId());
        dto.setIsPublic(figure.getIsPublic());
        dto.setViewCount(figure.getViewCount());
        dto.setLikeCount(figure.getLikeCount());
        dto.setWantCount(figure.getWantCount());
        dto.setCreatedAt(figure.getCreatedAt());
        dto.setUpdatedAt(figure.getUpdatedAt());

        userRepository.findById(figure.getUserId()).ifPresent(user -> {
            dto.setUsername(user.getUsername());
        });

        if (figure.getCategoryId() != null) {
            categoryRepository.findById(figure.getCategoryId()).ifPresent(category -> {
                dto.setCategoryName(category.getName());
            });
        }

        List<FigureImage> images = figureImageRepository.findByFigureIdOrderBySortOrderAsc(figure.getId());
        if (!images.isEmpty()) {
            dto.setPrimaryImage(images.get(0).getImageUrl());
            dto.setImages(images.stream().map(FigureImage::getImageUrl).collect(Collectors.toList()));
        }

        List<Long> tagIds = figureTagRepository.findTagIdsByFigureId(figure.getId());
        dto.setTagIds(tagIds);
        if (!tagIds.isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(tagIds);
            dto.setTagNames(tags.stream().map(Tag::getName).collect(Collectors.toList()));
        }

        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId != null) {
            dto.setIsLiked(likeRepository.existsByFigureIdAndUserId(figure.getId(), currentUserId));
            dto.setIsWanted(wantRepository.existsByFigureIdAndUserId(figure.getId(), currentUserId));
        }

        return dto;
    }
}

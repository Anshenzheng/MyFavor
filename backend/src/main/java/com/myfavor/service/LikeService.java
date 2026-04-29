package com.myfavor.service;

import com.myfavor.entity.Figure;
import com.myfavor.entity.Like;
import com.myfavor.repository.FigureRepository;
import com.myfavor.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private FigureRepository figureRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public boolean toggleLike(Long figureId) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Figure figure = figureRepository.findById(figureId)
            .orElseThrow(() -> new RuntimeException("手办不存在"));

        if (likeRepository.existsByFigureIdAndUserId(figureId, userId)) {
            likeRepository.deleteByFigureIdAndUserId(figureId, userId);
            figure.setLikeCount(Math.max(0, figure.getLikeCount() - 1));
            figureRepository.save(figure);
            return false;
        } else {
            Like like = new Like();
            like.setFigureId(figureId);
            like.setUserId(userId);
            likeRepository.save(like);
            figure.setLikeCount(figure.getLikeCount() + 1);
            figureRepository.save(figure);
            return true;
        }
    }

    public boolean isLiked(Long figureId) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            return false;
        }
        return likeRepository.existsByFigureIdAndUserId(figureId, userId);
    }

    public long getLikeCount(Long figureId) {
        return likeRepository.countByFigureId(figureId);
    }
}

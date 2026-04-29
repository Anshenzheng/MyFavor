package com.myfavor.service;

import com.myfavor.entity.Figure;
import com.myfavor.entity.Want;
import com.myfavor.repository.FigureRepository;
import com.myfavor.repository.WantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WantService {
    @Autowired
    private WantRepository wantRepository;

    @Autowired
    private FigureRepository figureRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public boolean toggleWant(Long figureId) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Figure figure = figureRepository.findById(figureId)
            .orElseThrow(() -> new RuntimeException("手办不存在"));

        if (wantRepository.existsByFigureIdAndUserId(figureId, userId)) {
            wantRepository.deleteByFigureIdAndUserId(figureId, userId);
            figure.setWantCount(Math.max(0, figure.getWantCount() - 1));
            figureRepository.save(figure);
            return false;
        } else {
            Want want = new Want();
            want.setFigureId(figureId);
            want.setUserId(userId);
            wantRepository.save(want);
            figure.setWantCount(figure.getWantCount() + 1);
            figureRepository.save(figure);
            return true;
        }
    }

    public boolean isWanted(Long figureId) {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            return false;
        }
        return wantRepository.existsByFigureIdAndUserId(figureId, userId);
    }

    public long getWantCount(Long figureId) {
        return wantRepository.countByFigureId(figureId);
    }
}

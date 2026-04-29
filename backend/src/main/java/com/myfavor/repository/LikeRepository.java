package com.myfavor.repository;

import com.myfavor.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByFigureIdAndUserId(Long figureId, Long userId);
    boolean existsByFigureIdAndUserId(Long figureId, Long userId);
    long countByFigureId(Long figureId);
    void deleteByFigureIdAndUserId(Long figureId, Long userId);
    void deleteByFigureId(Long figureId);
    void deleteByUserId(Long userId);
}

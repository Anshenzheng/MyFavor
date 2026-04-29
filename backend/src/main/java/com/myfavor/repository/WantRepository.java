package com.myfavor.repository;

import com.myfavor.entity.Want;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WantRepository extends JpaRepository<Want, Long> {
    Optional<Want> findByFigureIdAndUserId(Long figureId, Long userId);
    boolean existsByFigureIdAndUserId(Long figureId, Long userId);
    long countByFigureId(Long figureId);
    void deleteByFigureIdAndUserId(Long figureId, Long userId);
    void deleteByFigureId(Long figureId);
    void deleteByUserId(Long userId);
}

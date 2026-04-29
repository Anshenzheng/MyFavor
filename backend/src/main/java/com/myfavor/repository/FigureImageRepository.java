package com.myfavor.repository;

import com.myfavor.entity.FigureImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FigureImageRepository extends JpaRepository<FigureImage, Long> {
    List<FigureImage> findByFigureIdOrderBySortOrderAsc(Long figureId);
    Optional<FigureImage> findByFigureIdAndIsPrimaryTrue(Long figureId);
    void deleteByFigureId(Long figureId);
    List<FigureImage> findByFigureId(Long figureId);
}

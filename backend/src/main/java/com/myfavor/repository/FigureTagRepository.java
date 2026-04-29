package com.myfavor.repository;

import com.myfavor.entity.FigureTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FigureTagRepository extends JpaRepository<FigureTag, Long> {
    List<FigureTag> findByFigureId(Long figureId);
    List<FigureTag> findByTagId(Long tagId);
    void deleteByFigureId(Long figureId);
    void deleteByFigureIdAndTagId(Long figureId, Long tagId);
    boolean existsByFigureIdAndTagId(Long figureId, Long tagId);
    
    @Query("SELECT ft.tagId FROM FigureTag ft WHERE ft.figureId = :figureId")
    List<Long> findTagIdsByFigureId(@Param("figureId") Long figureId);
}

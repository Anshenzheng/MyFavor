package com.myfavor.repository;

import com.myfavor.entity.Figure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FigureRepository extends JpaRepository<Figure, Long> {
    
    Page<Figure> findByIsPublicTrue(Pageable pageable);
    
    Page<Figure> findByUserId(Long userId, Pageable pageable);
    
    Page<Figure> findByUserIdAndIsPublic(Long userId, Boolean isPublic, Pageable pageable);
    
    Page<Figure> findByCategoryIdAndIsPublicTrue(Long categoryId, Pageable pageable);
    
    @Query("SELECT f FROM Figure f WHERE f.isPublic = true ORDER BY f.likeCount DESC, f.viewCount DESC")
    Page<Figure> findPopularFigures(Pageable pageable);
    
    @Query("SELECT f FROM Figure f WHERE f.isPublic = true ORDER BY f.viewCount DESC")
    Page<Figure> findMostViewedFigures(Pageable pageable);
    
    @Query("SELECT f FROM Figure f WHERE f.isPublic = true AND (LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Figure> searchPublicFigures(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT f FROM Figure f WHERE f.userId = :userId AND (LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Figure> searchUserFigures(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT f FROM Figure f JOIN FigureTag ft ON f.id = ft.figureId WHERE ft.tagId = :tagId AND f.isPublic = true")
    Page<Figure> findByTagIdAndIsPublicTrue(@Param("tagId") Long tagId, Pageable pageable);
    
    @Query("SELECT DISTINCT f FROM Figure f JOIN FigureTag ft ON f.id = ft.figureId WHERE f.userId = :userId AND ft.tagId = :tagId")
    Page<Figure> findByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId, Pageable pageable);
    
    @Query("SELECT f FROM Figure f WHERE f.isPublic = true ORDER BY f.likeCount DESC, f.createdAt DESC")
    List<Figure> findTop10ByOrderByLikeCountDesc(Pageable pageable);
    
    void deleteByUserId(Long userId);
}

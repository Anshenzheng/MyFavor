package com.myfavor.controller;

import com.myfavor.dto.*;
import com.myfavor.service.FigureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/figures")
@CrossOrigin(origins = "*")
public class FigureController {

    @Autowired
    private FigureService figureService;

    @GetMapping("/public")
    public ResponseEntity<?> getPublicFigures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FigureDTO> figures = figureService.getPublicFigures(pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyFigures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FigureDTO> figures = figureService.getMyFigures(pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserFigures(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FigureDTO> figures = figureService.getUserFigures(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularFigures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FigureDTO> figures = figureService.getPopularFigures(pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/most-viewed")
    public ResponseEntity<?> getMostViewedFigures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FigureDTO> figures = figureService.getMostViewedFigures(pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFigures(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FigureDTO> figures = figureService.searchPublicFigures(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/search/my")
    public ResponseEntity<?> searchMyFigures(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FigureDTO> figures = figureService.searchMyFigures(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getFiguresByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FigureDTO> figures = figureService.getFiguresByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<?> getFiguresByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FigureDTO> figures = figureService.getFiguresByTag(tagId, pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(figures, figures.getContent())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFigureById(@PathVariable Long id) {
        try {
            FigureDTO figure = figureService.getFigureById(id);
            return ResponseEntity.ok(ApiResponse.success(figure));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createFigure(@Valid @RequestBody FigureCreateRequest request) {
        try {
            FigureDTO figure = figureService.createFigure(request);
            return ResponseEntity.ok(ApiResponse.success(figure));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("创建失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFigure(@PathVariable Long id, @Valid @RequestBody FigureUpdateRequest request) {
        try {
            FigureDTO figure = figureService.updateFigure(id, request);
            return ResponseEntity.ok(ApiResponse.success(figure));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFigure(@PathVariable Long id) {
        try {
            figureService.deleteFigure(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("删除失败: " + e.getMessage()));
        }
    }
}

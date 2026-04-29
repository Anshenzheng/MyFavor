package com.myfavor.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FigureDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate purchaseDate;
    private Long userId;
    private String username;
    private Long categoryId;
    private String categoryName;
    private Boolean isPublic;
    private Integer viewCount;
    private Integer likeCount;
    private Integer wantCount;
    private String primaryImage;
    private List<String> images;
    private List<Long> tagIds;
    private List<String> tagNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isLiked;
    private Boolean isWanted;
}

package com.myfavor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class FigureCreateRequest {
    @NotBlank(message = "手办名称不能为空")
    @Size(max = 100, message = "手办名称不能超过100个字符")
    private String name;

    @Size(max = 2000, message = "描述不能超过2000个字符")
    private String description;

    private BigDecimal price;

    private LocalDate purchaseDate;

    private Long categoryId;

    private Boolean isPublic = true;

    private List<String> imageUrls;

    private List<String> tagNames;

    private List<Long> tagIds;
}

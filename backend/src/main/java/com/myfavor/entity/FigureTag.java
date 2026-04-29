package com.myfavor.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "figure_tags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"figureId", "tagId"})
})
public class FigureTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long figureId;

    @Column(nullable = false)
    private Long tagId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

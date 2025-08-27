package com.woojin.autotil.til.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TilTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String markdownContent;
    private Boolean isPublic;
    private LocalDateTime createdAt;

    @Builder
    public TilTemplate(String title, String markdownContent, Boolean isPublic) {
        this.title = title;
        this.markdownContent = markdownContent;
        this.isPublic = isPublic;
        this.createdAt = LocalDateTime.now();
    }
}

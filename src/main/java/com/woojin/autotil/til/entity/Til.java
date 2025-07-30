package com.woojin.autotil.til.entity;

import com.woojin.autotil.user.entity.User;
import com.woojin.autotil.repo.entity.GitRepository;
import com.woojin.autotil.til.enums.TilStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Til {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate publishedAt;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Enumerated(value = EnumType.STRING)
    private TilStatus status = TilStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private GitRepository repository;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public void publish(){
        this.status = TilStatus.PUBLISHED;
        this.publishedAt = LocalDate.now();
    }
}

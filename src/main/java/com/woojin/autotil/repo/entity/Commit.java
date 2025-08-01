package com.woojin.autotil.repo.entity;

import com.woojin.autotil.til.entity.Til;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commitSha;

    private String message;

    private LocalDateTime committedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "til_id", nullable = false)
    private Til til;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id")
    private GitRepository gitRepository;
}

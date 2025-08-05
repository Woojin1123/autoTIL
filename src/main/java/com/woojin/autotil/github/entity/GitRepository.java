package com.woojin.autotil.github.entity;

import com.woojin.autotil.auth.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "repositories")
@NoArgsConstructor
public class GitRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long githubRepoId;
    private String repoName;
    private String repoOwner;
    private String repoUrl;
    private LocalDateTime pushedAt;
    private Boolean isTracked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "gitRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commit> commits = new ArrayList<>();

    @Builder
    public GitRepository(Long id, Long githubRepoId, String repoName, String repoOwner, String repoUrl, LocalDateTime pushed_at, User user) {
        this.id = id;
        this.githubRepoId = githubRepoId;
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.repoUrl = repoUrl;
        this.pushedAt = pushed_at;
        this.user = user;
    }

    public void updatePushedAt(LocalDateTime pushedAt) {
        this.pushedAt = pushedAt;
    }

    public void updateTracked(Boolean isTracked) {
        this.isTracked = isTracked;
    }
}

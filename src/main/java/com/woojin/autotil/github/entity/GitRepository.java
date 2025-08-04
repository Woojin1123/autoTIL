package com.woojin.autotil.github.entity;

import com.woojin.autotil.auth.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String repoName;
    private String repoOwner;
    private String repoUrl;
    private Boolean isTracked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "gitRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commit> commits = new ArrayList<>();

    @Builder
    public GitRepository(Long id, String repoName, String repoOwner, String repoUrl, User user) {
        this.id = id;
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.repoUrl = repoUrl;
        this.user = user;
    }
}

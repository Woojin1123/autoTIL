package com.woojin.autotil.repo.entity;

import com.woojin.autotil.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "repositories")
public class GitRepository {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String repoName;
    private String repoOwner;
    private String repoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "gitRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commit> commits = new ArrayList<>();
}

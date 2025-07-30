package com.woojin.autotil.repo.entity;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.til.entity.Til;
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

    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Til> tils = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

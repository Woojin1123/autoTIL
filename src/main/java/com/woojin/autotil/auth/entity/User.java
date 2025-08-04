package com.woojin.autotil.auth.entity;

import com.woojin.autotil.auth.enums.Role;
import com.woojin.autotil.github.entity.GitRepository;
import com.woojin.autotil.til.entity.Til;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private Long githubId;
    private String githubToken;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<GitRepository> repositories = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Til> tils = new ArrayList<>();

    @Builder
    public User(Long id, Long githubId, String loginId, Role role, String githubToken){
        this.id = id;
        this.githubId = githubId;
        this.loginId = loginId;
        this.role = role;
        this.githubToken = githubToken;
    }
    public void revokedGithubToken(){
        this.githubToken = null;
    }
}

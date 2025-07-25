package com.woojin.autotil.user.entity;

import com.woojin.autotil.user.enums.Role;
import com.woojin.autotil.repo.entity.GitRepository;
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
    private String githubId;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GitRepository> repositories = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Til> tils = new ArrayList<>();

    @Builder
    public User(Long id, String githubId, Role role){
        this.id = id;
        this.githubId = githubId;
        this.role = role;
    }
}

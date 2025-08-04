package com.woojin.autotil.til.entity;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.github.entity.Commit;
import com.woojin.autotil.til.enums.TilStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @OneToMany(mappedBy = "til", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commit> commits = new ArrayList<>();

    public void publish(){
        this.status = TilStatus.PUBLISHED;
        this.publishedAt = LocalDate.now();
    }
}

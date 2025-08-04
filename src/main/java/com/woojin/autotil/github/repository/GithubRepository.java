package com.woojin.autotil.github.repository;

import com.woojin.autotil.github.entity.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubRepository extends JpaRepository<GitRepository,Long> {
}

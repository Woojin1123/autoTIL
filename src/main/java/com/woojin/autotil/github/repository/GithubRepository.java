package com.woojin.autotil.github.repository;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.github.entity.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GithubRepository extends JpaRepository<GitRepository,Long> {
    List<GitRepository> findAllByUser(User authUser);


    List<GitRepository> findAllByUserIdAndGithubRepoIdIn(Long id, List<Long> repoIds);
}

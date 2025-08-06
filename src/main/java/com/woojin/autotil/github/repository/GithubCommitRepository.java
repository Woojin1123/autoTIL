package com.woojin.autotil.github.repository;

import com.woojin.autotil.github.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GithubCommitRepository extends JpaRepository<Commit, Long> {
    List<Commit> findAllByGitRepositoryId(Long id);
}

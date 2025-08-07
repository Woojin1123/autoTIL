package com.woojin.autotil.commit.repository;

import com.woojin.autotil.commit.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GithubCommitRepository extends JpaRepository<Commit, Long> {
    List<Commit> findAllByGitRepositoryId(Long id);

    Optional<Commit> findByCommitSha(String sha);
}

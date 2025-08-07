package com.woojin.autotil.repo.service;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.service.AuthService;
import com.woojin.autotil.repo.dto.RepoTrackRequest;
import com.woojin.autotil.repo.entity.GitRepository;
import com.woojin.autotil.repo.repository.GithubRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepoService {
    private final AuthService authService;
    private final GithubRepoRepository githubRepoRepository;

    @Transactional
    public List<Long> updateRepoTracked(RepoTrackRequest request) {
        User authUser = authService.getAuthUser();

        List<GitRepository> repos = githubRepoRepository.findAllByUserIdAndGithubRepoIdIn(authUser.getId(), request.getRepoIds());

        repos.forEach(repo -> repo.updateTracked(true));

        githubRepoRepository.saveAll(repos);

        return repos.stream()
                .filter(GitRepository::getIsTracked)
                .map(GitRepository::getId)
                .toList();
    }
}

package com.woojin.autotil.til.service;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.service.AuthService;
import com.woojin.autotil.github.service.GithubService;
import com.woojin.autotil.til.dto.TilRequest;
import com.woojin.autotil.til.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TilService {
    private final TilRepository tilRepository;
    private final GithubService githubService;
    private final AuthService authService;

    public List<String> createTilTemplate(TilRequest tilRequest) {
        User user = authService.getAuthUser();

        return tilRequest.getCommitsha().stream()
                .map(
                        githubService::getCommitDiff)
                .toList();
    }
}

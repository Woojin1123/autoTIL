package com.woojin.autotil.til.service;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.service.AuthService;
import com.woojin.autotil.github.service.GithubService;
import com.woojin.autotil.til.dto.TilResponse;
import com.woojin.autotil.til.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TilService {
    private final TilRepository tilRepository;
    private final GithubService githubService;
    private final AuthService authService;

    public TilResponse createTilTemplate() {
        User user = authService.getAuthUser();


        return null;
    }
}

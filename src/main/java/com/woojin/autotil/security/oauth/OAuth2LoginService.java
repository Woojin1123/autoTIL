package com.woojin.autotil.security.oauth;

import com.woojin.autotil.user.entity.User;
import com.woojin.autotil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {
    private final EncryptService encryptService;
    private final UserRepository userRepository;

    public User oAuth2Login(CustomOAuth2User user){
        String accessToken = user.getAccessToken();
        String encryptToken = encryptService.encryptToken(accessToken);

        return userRepository.findByGithubId(user.getProviderId())
                .orElseGet(()->userRepository.save(
                        User.builder()
                                .githubId(user.getProviderId())
                                .loginId(user.getLoginId())
                                .role(user.getRole())
                                .githubToken(encryptToken)
                                .build()
                ));
    }
}

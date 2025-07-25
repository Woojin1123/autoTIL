package com.woojin.autotil.security.oauth;

import com.woojin.autotil.user.dto.AuthUser;
import com.woojin.autotil.user.entity.User;
import com.woojin.autotil.user.enums.Role;
import com.woojin.autotil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // Github Resource 서버에서 제공되는 유저 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response oAuth2Response = new GithubResponse(oAuth2User.getAttributes());

        User user = userRepository.findByGithubId(oAuth2Response.getProviderId())
                .orElseGet(()-> userRepository.save(
                        User.builder()
                                .githubId(oAuth2Response.getProviderId())
                                .loginId(oAuth2Response.getUsername())
                                .role(Role.ROLE_USER)
                                .build()
                    )
                );

        AuthUser authUser = AuthUser.builder()
                .id(user.getId()) // db pk
                .loginId(user.getLoginId()) // github id
                .role(user.getRole())
                .build();


        return new CustomOAuth2User(authUser);
    }
}

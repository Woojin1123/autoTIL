package com.woojin.autotil.security.oauth;

import com.woojin.autotil.auth.dto.AuthUser;
import com.woojin.autotil.auth.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // Github Resource 서버에서 제공되는 유저 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
        AuthUser authUser = AuthUser.builder()
                .loginId(oAuth2Response.getUsername()) // github id
                .githubId(oAuth2Response.getProviderId())
                .role(Role.ROLE_USER)
                .build();
        return new CustomOAuth2User(authUser,userRequest.getAccessToken().getTokenValue());
    }
}

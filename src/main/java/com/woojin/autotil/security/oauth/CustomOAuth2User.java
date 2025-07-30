package com.woojin.autotil.security.oauth;

import com.woojin.autotil.auth.dto.AuthUser;
import com.woojin.autotil.auth.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final AuthUser authUser;
    private final String accessToken;

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(authUser.getRole().name()));
    }

    @Override
    public String getName() {
        return authUser.getLoginId();
    }

    public Long getId() {
        return authUser.getId();
    }
    public Role getRole(){
        return authUser.getRole();
    }

    public Long getProviderId(){
        return authUser.getGithubId();
    }
    public String getLoginId(){
        return authUser.getLoginId();
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}

package com.woojin.autotil.security.oauth;

import java.util.Map;

public class GithubResponse implements OAuth2Response{
    private final Map<String, Object> attribute;

    public GithubResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public Long getProviderId() {
        return Long.valueOf(attribute.get("id").toString());
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getUsername() {
        return attribute.get("login").toString();
    }

    @Override
    public String getNickname() {
        return attribute.get("name").toString();
    }

}

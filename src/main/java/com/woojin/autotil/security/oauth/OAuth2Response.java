package com.woojin.autotil.security.oauth;

public interface OAuth2Response {
    String getProvider(); // 제공자 (EX. naver, google, etc)
    String getProviderId(); // 제공자에서 발급해주는 아이디
    String getEmail(); // 이메일
    String getUsername(); // 사용자 이름
    String getNickname();
}

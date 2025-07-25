package com.woojin.autotil.security.oauth;

public interface OAuth2Response {
    String getProvider(); // 제공자 (EX. naver, google, etc)
    Long getProviderId(); // 제공자에서 발급해주는 고유 아이디 (pk)
    String getEmail(); // 이메일
    String getUsername(); // 사용자가 로그인시 사용하는 아이디
    String getNickname(); // 사용자 닉네임
}

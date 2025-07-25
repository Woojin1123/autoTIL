package com.woojin.autotil.user.dto;

import com.woojin.autotil.user.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUser {
    private Long id;
    private String loginId;
    private Role role;
}

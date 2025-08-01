package com.woojin.autotil.auth.dto;

import com.woojin.autotil.auth.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUser {
    private Long id;
    private String loginId; // Github ID
    private Role role;
    private Long githubId;
}

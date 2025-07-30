package com.woojin.autotil.auth.controller;

import com.woojin.autotil.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(HttpServletRequest request, HttpServletResponse response){

        return ResponseEntity.ok(null);
    }
}

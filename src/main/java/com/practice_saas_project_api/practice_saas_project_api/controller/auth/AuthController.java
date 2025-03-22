
package com.practice_saas_project_api.practice_saas_project_api.controller.auth;

import com.practice_saas_project_api.practice_saas_project_api.dto.request.LoginRequest;
import com.practice_saas_project_api.practice_saas_project_api.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        ResponseEntity<String> loginResponse = authService.login(request.getEmail(), request.getPassword(), response);
        return loginResponse;
    }
} 
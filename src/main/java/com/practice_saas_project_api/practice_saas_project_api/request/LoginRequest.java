package com.practice_saas_project_api.practice_saas_project_api.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
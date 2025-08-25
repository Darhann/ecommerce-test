package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}

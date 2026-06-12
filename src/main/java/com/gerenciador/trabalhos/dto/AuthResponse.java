package com.gerenciador.trabalhos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String nome;
    private String email;
    private String role;
}




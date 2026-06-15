package com.gerenciador.trabalhos.dto;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String role) {
}

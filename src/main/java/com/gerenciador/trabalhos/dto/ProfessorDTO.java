package com.gerenciador.trabalhos.dto;



public record ProfessorDTO(
        Long id,
        String nome,
        String email,
        String password,
        String role,
        String areaAtuacao) {
}

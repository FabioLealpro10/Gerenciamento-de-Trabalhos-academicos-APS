package com.gerenciador.trabalhos.dto;



public record AlunoDTO(
        Long id,
        String nome,
        String email,
        String password,
        String role,
        String turma) {
}
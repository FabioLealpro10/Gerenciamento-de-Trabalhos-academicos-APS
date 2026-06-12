package com.gerenciador.trabalhos.dto;


public record EntregaTrabalhoRequestDTO(
        String dataEntrega,
        Long trabalhoId,
        Long alunoId) {
}
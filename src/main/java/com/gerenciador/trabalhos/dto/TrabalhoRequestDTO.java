package com.gerenciador.trabalhos.dto;


public record TrabalhoRequestDTO(
        String titulo,
        String descricao,
        String dataInicio,
        String dataFim,
        Long disciplinaId) {
}
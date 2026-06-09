package com.gerenciador.trabalhos.dto;


public record TrabalhoRequestDTO(
        String titulo,
        String descricao,
        String linkArquivoTrabalho,
        String dataInicio,
        String dataFim,
        Long disciplinaId) {
}
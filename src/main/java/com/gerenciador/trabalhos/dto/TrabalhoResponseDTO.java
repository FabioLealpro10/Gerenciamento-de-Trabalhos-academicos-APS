package com.gerenciador.trabalhos.dto;


public record TrabalhoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        String linkArquivoTrabalho,
        String dataInicio,
        String dataFim,
        Long disciplinaId,
        String disciplinaNome) {
}
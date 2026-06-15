package com.gerenciador.trabalhos.dto;


public record TrabalhoResponseDTO(
        Long id,
        String titulo,
        String descricao,
<<<<<<< HEAD
        String caminhoArquivoPdf,
=======
        String linkArquivoTrabalho,
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
        String dataInicio,
        String dataFim,
        Long disciplinaId,
        String disciplinaNome) {
}
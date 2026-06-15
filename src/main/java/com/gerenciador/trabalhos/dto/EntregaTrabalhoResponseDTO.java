package com.gerenciador.trabalhos.dto;


public record EntregaTrabalhoResponseDTO(
        Long id,
<<<<<<< HEAD
        String caminhoArquivoPdf,
=======
        String linkArquivo,
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
        String dataEntrega,
        Float nota,
        String feedback,
        String mensagem,
        Long trabalhoId,
        String trabalhoTitulo,
        String disciplinaNome,
        String professorNome,
        Long alunoId,
        String alunoNome) {
}
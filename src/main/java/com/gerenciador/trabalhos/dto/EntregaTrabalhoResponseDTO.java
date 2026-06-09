package com.gerenciador.trabalhos.dto;


public record EntregaTrabalhoResponseDTO(
        Long id,
        String linkArquivo,
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
package com.gerenciador.trabalhos.dto;



public record DisciplinaRequestDTO(
        String nome,
        String dataInicio,
        String dataFim,
        Long idProfessor) {
}
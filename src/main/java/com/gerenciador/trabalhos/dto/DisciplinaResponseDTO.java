package com.gerenciador.trabalhos.dto;

import java.util.List;

public record DisciplinaResponseDTO(
        Long id,
        String nome,
        String dataInicio,
        String dataFim,
        String professor,
        List<String> alunosMatriculados) {
}
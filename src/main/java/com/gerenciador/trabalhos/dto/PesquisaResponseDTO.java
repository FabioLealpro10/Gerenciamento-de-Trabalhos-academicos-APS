package com.gerenciador.trabalhos.dto;

import java.util.List;

public record PesquisaResponseDTO(
        String pesquisa,
        List<AlunoDTO> alunos,
        List<ProfessorDTO> professores,
        List<TrabalhoResponseDTO> trabalhos,
        List<DisciplinaResponseDTO> disciplinas,
        long totalResultados) {
}

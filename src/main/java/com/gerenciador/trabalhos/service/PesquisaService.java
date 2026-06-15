package com.gerenciador.trabalhos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerenciador.trabalhos.dto.AlunoDTO;
import com.gerenciador.trabalhos.dto.DisciplinaResponseDTO;
import com.gerenciador.trabalhos.dto.PesquisaResponseDTO;
import com.gerenciador.trabalhos.dto.ProfessorDTO;
import com.gerenciador.trabalhos.dto.TrabalhoResponseDTO;
import com.gerenciador.trabalhos.model.Aluno;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.Professor;
import com.gerenciador.trabalhos.model.Trabalho;
import com.gerenciador.trabalhos.repository.AlunoRepository;
import com.gerenciador.trabalhos.repository.DisciplinaRepository;
import com.gerenciador.trabalhos.repository.ProfessorRepository;
import com.gerenciador.trabalhos.repository.TrabalhoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PesquisaService {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final TrabalhoRepository trabalhoRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Transactional(readOnly = true)
    public PesquisaResponseDTO pesquisar(String pesquisa) {
        if (pesquisa == null || pesquisa.isBlank()) {
            throw new RuntimeException("Informe o parâmetro 'pesquisa' com a subpalavra desejada");
        }

        String termo = pesquisa.trim();

        List<AlunoDTO> alunos = alunoRepository.findByNomeContainingIgnoreCase(termo)
                .stream()
                .map(this::toAlunoDTO)
                .toList();

        List<ProfessorDTO> professores = professorRepository.findByNomeContainingIgnoreCase(termo)
                .stream()
                .map(this::toProfessorDTO)
                .toList();

        List<TrabalhoResponseDTO> trabalhos = trabalhoRepository.findByTituloContainingIgnoreCase(termo)
                .stream()
                .map(this::toTrabalhoDTO)
                .toList();

        List<DisciplinaResponseDTO> disciplinas = disciplinaRepository.findByNomeContainingIgnoreCase(termo)
                .stream()
                .map(this::toDisciplinaDTO)
                .toList();

        long total = alunos.size() + professores.size() + trabalhos.size() + disciplinas.size();

        return new PesquisaResponseDTO(termo, alunos, professores, trabalhos, disciplinas, total);
    }

    private AlunoDTO toAlunoDTO(Aluno aluno) {
        return new AlunoDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getPassword(),
                aluno.getRole(),
                aluno.getTurma());
    }

    private ProfessorDTO toProfessorDTO(Professor professor) {
        return new ProfessorDTO(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                professor.getPassword(),
                professor.getRole(),
                professor.getAreaAtuacao());
    }

    private TrabalhoResponseDTO toTrabalhoDTO(Trabalho trabalho) {
        return new TrabalhoResponseDTO(
                trabalho.getId(),
                trabalho.getTitulo(),
                trabalho.getDescricao(),
                trabalho.getCaminhoArquivoPdf(),
                trabalho.getDataInicio(),
                trabalho.getDataFim(),
                trabalho.getDisciplina().getId(),
                trabalho.getDisciplina().getNome());
    }

    private DisciplinaResponseDTO toDisciplinaDTO(Disciplina disciplina) {
        List<String> alunosMatriculados = disciplina.getMatriculas() != null
                ? disciplina.getMatriculas().stream()
                        .map(m -> m.getAluno().getNome())
                        .toList()
                : List.of();

        return new DisciplinaResponseDTO(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDataInicio(),
                disciplina.getDataFim(),
                disciplina.getProfessor().getNome(),
                alunosMatriculados);
    }
}

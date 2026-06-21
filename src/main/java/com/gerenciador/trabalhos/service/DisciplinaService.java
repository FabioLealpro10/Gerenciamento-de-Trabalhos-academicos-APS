package com.gerenciador.trabalhos.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerenciador.trabalhos.dto.DisciplinaRequestDTO;
import com.gerenciador.trabalhos.dto.DisciplinaResponseDTO;
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.exception.MensagensExclusao;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.Professor;
import com.gerenciador.trabalhos.repository.DisciplinaRepository;
import com.gerenciador.trabalhos.repository.MatriculaRepository;
import com.gerenciador.trabalhos.repository.ProfessorRepository;
import com.gerenciador.trabalhos.repository.TrabalhoRepository;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final MatriculaRepository matriculaRepository;
    private final TrabalhoRepository trabalhoRepository;

    public DisciplinaService(
            DisciplinaRepository disciplinaRepository,
            ProfessorRepository professorRepository,
            MatriculaRepository matriculaRepository,
            TrabalhoRepository trabalhoRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
        this.matriculaRepository = matriculaRepository;
        this.trabalhoRepository = trabalhoRepository;
    }

    public Disciplina criar(DisciplinaRequestDTO dto) {

        Professor professor = professorRepository.findById(dto.idProfessor())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(dto.nome());
        disciplina.setDataInicio(dto.dataInicio());
        disciplina.setDataFim(dto.dataFim());
        disciplina.setProfessor(professor);

        return disciplinaRepository.save(disciplina);
    }

    public DisciplinaResponseDTO buscarPorId(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        List<String> alunos = disciplina.getMatriculas()
                .stream()
                .map(m -> m.getAluno().getNome())
                .toList();

        return new DisciplinaResponseDTO(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDataInicio(),
                disciplina.getDataFim(),
                disciplina.getProfessor().getNome(),
                alunos);
    }

    public PageResponseDTO<DisciplinaResponseDTO> listarTodas(int page, int size) {
        return PageResponseDTO.from(
                disciplinaRepository.findAll(PageRequest.of(page, size))
                        .map(d -> buscarPorId(d.getId())));
    }

    public PageResponseDTO<DisciplinaResponseDTO> listarPorProfessor(Long professorId, int page, int size) {
        return PageResponseDTO.from(
                disciplinaRepository.findByProfessorId(professorId, PageRequest.of(page, size))
                        .map(d -> buscarPorId(d.getId())));
    }

    public PageResponseDTO<DisciplinaResponseDTO> pesquisarPorNome(String nome, int page, int size) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Informe o parâmetro 'nome' com a subpalavra desejada");
        }

        return PageResponseDTO.from(
                disciplinaRepository.findByNomeContainingIgnoreCase(nome.trim(), PageRequest.of(page, size))
                        .map(d -> buscarPorId(d.getId())));
    }

    public DisciplinaResponseDTO atualizar(Long id, DisciplinaRequestDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        if (dto.nome() != null) {
            disciplina.setNome(dto.nome());
        }
        if (dto.dataInicio() != null) {
            disciplina.setDataInicio(dto.dataInicio());
        }
        if (dto.dataFim() != null) {
            disciplina.setDataFim(dto.dataFim());
        }
        if (dto.idProfessor() != null) {
            Professor professor = professorRepository.findById(dto.idProfessor())
                    .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
            disciplina.setProfessor(professor);
        }

        disciplinaRepository.save(disciplina);

        return buscarPorId(disciplina.getId());
    }

    @Transactional
    public void deletar(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        int matriculas = matriculaRepository.findByDisciplinaId(id).size();
        int trabalhos = trabalhoRepository.findByDisciplinaId(id).size();

        if (matriculas > 0 && trabalhos > 0) {
            throw new RuntimeException(MensagensExclusao.disciplinaComVinculos(matriculas, trabalhos));
        }
        if (matriculas > 0) {
            throw new RuntimeException(MensagensExclusao.disciplinaComMatriculas(matriculas));
        }
        if (trabalhos > 0) {
            throw new RuntimeException(MensagensExclusao.disciplinaComTrabalhos(trabalhos));
        }

        disciplinaRepository.delete(disciplina);
    }
}

package com.gerenciador.trabalhos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gerenciador.trabalhos.dto.DisciplinaRequestDTO;
import com.gerenciador.trabalhos.dto.DisciplinaResponseDTO;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.Professor;
import com.gerenciador.trabalhos.repository.DisciplinaRepository;
import com.gerenciador.trabalhos.repository.MatriculaRepository;
import com.gerenciador.trabalhos.repository.ProfessorRepository;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final MatriculaRepository matriculaRepository;

    public DisciplinaService(DisciplinaRepository d, ProfessorRepository p, MatriculaRepository m) {
        this.disciplinaRepository = d;
        this.professorRepository = p;
        this.matriculaRepository = m;
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

    public List<DisciplinaResponseDTO> listarTodas() {
        return disciplinaRepository.findAll()
                .stream()
                .map(d -> buscarPorId(d.getId()))
                .toList();
    }

    public List<DisciplinaResponseDTO> listarPorProfessor(Long professorId) {
        return disciplinaRepository.findByProfessorId(professorId)
                .stream()
                .map(d -> buscarPorId(d.getId()))
                .toList();
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
}

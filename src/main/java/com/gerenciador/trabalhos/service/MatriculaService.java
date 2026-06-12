package com.gerenciador.trabalhos.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.gerenciador.trabalhos.model.Aluno;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.Matricula;
import com.gerenciador.trabalhos.repository.AlunoRepository;
import com.gerenciador.trabalhos.repository.DisciplinaRepository;
import com.gerenciador.trabalhos.repository.MatriculaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public Matricula matricularAluno(Long alunoId, Long disciplinaId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        // Verificar se o aluno já está matriculado nesta disciplina
        matriculaRepository.findByAlunoIdAndDisciplinaId(alunoId, disciplinaId)
                .ifPresent(m -> {
                    throw new RuntimeException("Aluno já está matriculado nesta disciplina");
                });

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);
        matricula.setData(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        return matriculaRepository.save(matricula);
    }

    public void removerMatricula(Long alunoId, Long disciplinaId) {
        Matricula matricula = matriculaRepository.findByAlunoIdAndDisciplinaId(alunoId, disciplinaId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        matriculaRepository.delete(matricula);
    }
}




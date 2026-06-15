package com.gerenciador.trabalhos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerenciador.trabalhos.model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByAlunoId(Long alunoId);

    Page<Matricula> findByAlunoId(Long alunoId, Pageable pageable);

    List<Matricula> findByDisciplinaId(Long disciplinaId);

    List<Matricula> findByDisciplinaProfessorId(Long professorId);

    Optional<Matricula> findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId);
}

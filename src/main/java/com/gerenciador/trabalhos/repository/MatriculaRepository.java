package com.gerenciador.trabalhos.repository;

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerenciador.trabalhos.model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByAlunoId(Long alunoId);

<<<<<<< HEAD
    Page<Matricula> findByAlunoId(Long alunoId, Pageable pageable);

=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    List<Matricula> findByDisciplinaId(Long disciplinaId);

    List<Matricula> findByDisciplinaProfessorId(Long professorId);

    Optional<Matricula> findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId);
}

package com.gerenciador.trabalhos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerenciador.trabalhos.model.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    
    List<Disciplina> findByProfessorId(Long professorId);

<<<<<<< HEAD
    Page<Disciplina> findByProfessorId(Long professorId, Pageable pageable);

    List<Disciplina> findByNomeContainingIgnoreCase(String nome);

    Page<Disciplina> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
=======
    List<Disciplina> findByNomeContainingIgnoreCase(String nome);
    
    Page<Disciplina> findAll(Pageable pageable);
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
}
package com.gerenciador.trabalhos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerenciador.trabalhos.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findByNomeContainingIgnoreCase(String nome);
<<<<<<< HEAD

    Page<Professor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
=======
    
    Page<Professor> findAll(Pageable pageable);
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
}
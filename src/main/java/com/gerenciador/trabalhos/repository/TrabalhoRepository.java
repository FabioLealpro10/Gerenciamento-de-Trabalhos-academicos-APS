package com.gerenciador.trabalhos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gerenciador.trabalhos.model.Trabalho;

public interface TrabalhoRepository extends JpaRepository<Trabalho, Long> {

    // retorna todos os trabalhos vinculados a uma disciplina específica
    List<Trabalho> findByDisciplinaId(Long disciplinaId);

<<<<<<< HEAD
    Page<Trabalho> findByDisciplinaId(Long disciplinaId, Pageable pageable);

    List<Trabalho> findByTituloContainingIgnoreCase(String titulo);

    Page<Trabalho> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
=======
    List<Trabalho> findByTituloContainingIgnoreCase(String titulo);
    
    Page<Trabalho> findAll(Pageable pageable);
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
}
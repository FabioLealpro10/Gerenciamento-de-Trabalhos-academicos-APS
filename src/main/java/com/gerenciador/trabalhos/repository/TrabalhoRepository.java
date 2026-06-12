package com.gerenciador.trabalhos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gerenciador.trabalhos.model.Trabalho;

public interface TrabalhoRepository extends JpaRepository<Trabalho, Long> {

    // retorna todos os trabalhos vinculados a uma disciplina específica
    List<Trabalho> findByDisciplinaId(Long disciplinaId);

    List<Trabalho> findByTituloContainingIgnoreCase(String titulo);
    
    Page<Trabalho> findAll(Pageable pageable);
}
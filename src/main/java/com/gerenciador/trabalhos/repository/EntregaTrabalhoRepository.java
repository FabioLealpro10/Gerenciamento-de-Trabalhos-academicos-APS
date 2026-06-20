package com.gerenciador.trabalhos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gerenciador.trabalhos.model.EntregaTrabalho;

public interface EntregaTrabalhoRepository extends JpaRepository<EntregaTrabalho, Long> {

    List<EntregaTrabalho> findByTrabalhoId(Long trabalhoId);

    List<EntregaTrabalho> findByAlunoId(Long alunoId);

    void deleteByAlunoId(Long alunoId);

    Optional<EntregaTrabalho> findByAlunoIdAndTrabalhoId(Long alunoId, Long trabalhoId);

    Page<EntregaTrabalho> findByTrabalhoId(Long trabalhoId, Pageable pageable);

    @Query("SELECT e FROM EntregaTrabalho e " +
           "JOIN e.trabalho t " +
           "JOIN t.disciplina d " +
           "JOIN d.professor p " +
           "JOIN e.aluno a " +
           "WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(t.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<EntregaTrabalho> findByPesquisa(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT e FROM EntregaTrabalho e " +
           "JOIN e.trabalho t " +
           "JOIN t.disciplina d " +
           "JOIN d.professor p " +
           "JOIN e.aluno a " +
           "WHERE t.id = :trabalhoId AND (" +
           "LOWER(a.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(t.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<EntregaTrabalho> findByTrabalhoIdAndPesquisa(
            @Param("trabalhoId") Long trabalhoId,
            @Param("termo") String termo,
            Pageable pageable);
}

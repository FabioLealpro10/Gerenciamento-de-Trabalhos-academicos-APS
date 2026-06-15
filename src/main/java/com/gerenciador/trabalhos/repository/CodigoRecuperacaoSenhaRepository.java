package com.gerenciador.trabalhos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gerenciador.trabalhos.model.CodigoRecuperacaoSenha;

public interface CodigoRecuperacaoSenhaRepository extends JpaRepository<CodigoRecuperacaoSenha, Long> {
    Optional<CodigoRecuperacaoSenha> findByEmail(String email);

    void deleteByEmail(String email);
}

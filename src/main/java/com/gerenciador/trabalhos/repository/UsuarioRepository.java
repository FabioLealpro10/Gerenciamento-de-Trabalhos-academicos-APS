package com.gerenciador.trabalhos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gerenciador.trabalhos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}




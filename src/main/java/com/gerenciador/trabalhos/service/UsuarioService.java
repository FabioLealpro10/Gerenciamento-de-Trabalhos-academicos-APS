package com.gerenciador.trabalhos.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gerenciador.trabalhos.model.Aluno;
import com.gerenciador.trabalhos.model.Professor;
import com.gerenciador.trabalhos.model.Usuario;
import com.gerenciador.trabalhos.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        String role = toAuthority(resolveRoleFromUsuario(usuario));
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        return new User(usuario.getEmail(), usuario.getPassword(), Collections.singleton(authority));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public void validarEmailDisponivel(String email, Long idExcluir) {
        if (email == null || email.isBlank()) {
            return;
        }
        boolean emUso = idExcluir == null
                ? usuarioRepository.existsByEmail(email)
                : usuarioRepository.existsByEmailAndIdNot(email, idExcluir);
        if (emUso) {
            throw new IllegalArgumentException("Email já está em uso por outro usuário");
        }
    }

    public Usuario registrarUsuario(String nome, String email, String rawPassword, String role) {
        validarEmailDisponivel(email, null);
        String encoded = passwordEncoder.encode(rawPassword);
        String normalized = toDatabaseRole(role);
        Usuario usuario = Usuario.builder()
                .nome(nome)
                .email(email)
                .password(encoded)
                .role(normalized)
                .build();
        return usuarioRepository.save(usuario);
    }

    /** Valor gravado na coluna {@code role} do banco (ADMIN, ALUNO, PROFESSOR). */
    public String toDatabaseRole(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }
        String r = role.trim();
        if (r.startsWith("ROLE_")) {
            r = r.substring(5);
        }
        return switch (r.toUpperCase()) {
            case "ADMIN" -> "ADMIN";
            case "ALUNO" -> "ALUNO";
            case "PROFESSOR", "PROFESS" -> "PROFESSOR";
            default -> r.toUpperCase();
        };
    }

    /**
     * Authority usada pelo Spring Security. Deve bater com {@code hasRole('...')} dos controllers:
     * ADMIN → ROLE_ADMIN; ALUNO → ROLE_ALUNO; PROFESSOR → ROLE_PROFESSOR.
     */
    public String toAuthority(String role) {
        if (role == null || role.isBlank()) {
            return "ROLE_USER";
        }
        String r = role.trim();
        if (r.startsWith("ROLE_")) {
            r = r.substring(5);
        }
        return switch (r.toUpperCase()) {
            case "ADMIN" -> "ROLE_ADMIN";
            case "ALUNO" -> "ROLE_ALUNO";
            case "PROFESSOR", "PROFESS" -> "ROLE_PROFESSOR";
            default -> "ROLE_" + r.toUpperCase();
        };
    }

    private String resolveRoleFromUsuario(Usuario usuario) {
        if (usuario.getRole() != null && !usuario.getRole().isBlank()) {
            return usuario.getRole();
        }
        if (usuario instanceof Aluno) {
            return "ALUNO";
        }
        if (usuario instanceof Professor) {
            return "PROFESSOR";
        }
        return "USER";
    }
}




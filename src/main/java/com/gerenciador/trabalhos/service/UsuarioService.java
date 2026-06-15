package com.gerenciador.trabalhos.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gerenciador.trabalhos.dto.AdminRequestDTO;
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.dto.UsuarioCredenciaisUpdateDTO;
import com.gerenciador.trabalhos.dto.UsuarioResponseDTO;
import com.gerenciador.trabalhos.model.Aluno;
import com.gerenciador.trabalhos.model.Professor;
import com.gerenciador.trabalhos.model.Usuario;
import com.gerenciador.trabalhos.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    public static final long ADMIN_PRINCIPAL_ID = 1L;

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

    public UsuarioResponseDTO cadastrarAdmin(AdminRequestDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new RuntimeException("Email é obrigatório");
        }
        if (dto.password() == null || dto.password().isBlank()) {
            throw new RuntimeException("Senha é obrigatória");
        }

        validarEmailDisponivel(dto.email(), null);

        Usuario admin = Usuario.builder()
                .nome(dto.nome().trim())
                .email(dto.email().trim())
                .password(passwordEncoder.encode(dto.password()))
                .role("ADMIN")
                .build();

        return toResponseDTO(usuarioRepository.save(admin));
    }

    public PageResponseDTO<UsuarioResponseDTO> listarAdmins(int page, int size) {
        return PageResponseDTO.from(
                usuarioRepository.findByRole("ADMIN", PageRequest.of(page, size))
                        .map(this::toResponseDTO));
    }

    public UsuarioResponseDTO buscarAdminPorId(Long id) {
        return toResponseDTO(buscarAdmin(id));
    }

    public UsuarioResponseDTO atualizarCredenciaisAdmin(
            Long id, UsuarioCredenciaisUpdateDTO dto, String emailLogado) {
        Usuario logado = buscarPorEmail(emailLogado);
        Usuario alvo = buscarAdmin(id);

        validarPermissaoAtualizarCredenciais(logado, alvo);

        return atualizarCredenciais(alvo, dto);
    }

    public void deletarAdmin(Long id, String emailLogado) {
        Usuario logado = buscarPorEmail(emailLogado);

        if (!isAdminPrincipal(logado)) {
            throw new RuntimeException("Apenas o administrador principal pode excluir outros administradores");
        }

        if (Long.valueOf(ADMIN_PRINCIPAL_ID).equals(id)) {
            throw new RuntimeException("O administrador principal não pode ser excluído");
        }

        Usuario admin = buscarAdmin(id);
        usuarioRepository.delete(admin);
    }

    private UsuarioResponseDTO atualizarCredenciais(Usuario usuario, UsuarioCredenciaisUpdateDTO dto) {
        boolean alterouEmail = dto.email() != null && !dto.email().isBlank();
        boolean alterouSenha = dto.password() != null && !dto.password().isBlank();

        if (!alterouEmail && !alterouSenha) {
            throw new RuntimeException("Informe ao menos email ou senha para atualização");
        }

        if (alterouEmail) {
            validarEmailDisponivel(dto.email(), usuario.getId());
            usuario.setEmail(dto.email().trim());
        }

        if (alterouSenha) {
            usuario.setPassword(passwordEncoder.encode(dto.password()));
        }

        usuarioRepository.save(usuario);

        return toResponseDTO(usuario);
    }

    private void validarPermissaoAtualizarCredenciais(Usuario logado, Usuario alvo) {
        if (isAdminPrincipal(logado)) {
            return;
        }

        if (!logado.getId().equals(alvo.getId())) {
            throw new RuntimeException("Você só pode alterar suas próprias credenciais");
        }
    }

    private Usuario buscarAdmin(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));

        if (!"ADMIN".equalsIgnoreCase(resolveRoleFromUsuario(usuario))) {
            throw new RuntimeException("Usuário informado não é administrador");
        }

        return usuario;
    }

    private boolean isAdminPrincipal(Usuario usuario) {
        return Long.valueOf(ADMIN_PRINCIPAL_ID).equals(usuario.getId());
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                resolveRoleFromUsuario(usuario));
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




package com.gerenciador.trabalhos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gerenciador.trabalhos.dto.AdminRequestDTO;
import com.gerenciador.trabalhos.dto.AuthRequest;
import com.gerenciador.trabalhos.dto.AuthResponse;
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.dto.UsuarioCredenciaisUpdateDTO;
import com.gerenciador.trabalhos.dto.UsuarioResponseDTO;
import com.gerenciador.trabalhos.model.Usuario;
import com.gerenciador.trabalhos.security.jwt.JwtTokenProvider;
import com.gerenciador.trabalhos.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        Usuario usuario = usuarioService.buscarPorEmail(request.getEmail());

        return ResponseEntity.ok(new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()));
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponseDTO<UsuarioResponseDTO>> listarAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(usuarioService.listarAdmins(page, size));
    }

    @GetMapping("/admins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> buscarAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarAdminPorId(id));
    }

    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> cadastrarAdmin(@RequestBody AdminRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarAdmin(dto));
    }

    @PatchMapping("/admins/{id}/credenciais")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> atualizarCredenciaisAdmin(
            @PathVariable Long id,
            @RequestBody UsuarioCredenciaisUpdateDTO dto) {
        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.atualizarCredenciaisAdmin(id, dto, emailLogado));
    }

    @DeleteMapping("/admins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarAdmin(@PathVariable Long id) {
        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        usuarioService.deletarAdmin(id, emailLogado);
        return ResponseEntity.noContent().build();
    }
}

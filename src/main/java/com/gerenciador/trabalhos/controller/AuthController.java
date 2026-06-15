package com.gerenciador.trabalhos.controller;

import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
=======
import org.springframework.security.access.prepost.PreAuthorize;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerenciador.trabalhos.dto.AuthRequest;
import com.gerenciador.trabalhos.dto.AuthResponse;
<<<<<<< HEAD
=======
import com.gerenciador.trabalhos.dto.RegisterRequest;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
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
<<<<<<< HEAD
=======

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> register(@RequestBody RegisterRequest request) {
        Usuario usuario = usuarioService.registrarUsuario(request.getNome(), request.getEmail(), request.getPassword(), "ADMIN");
        return ResponseEntity.ok(usuario);
    }
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
}




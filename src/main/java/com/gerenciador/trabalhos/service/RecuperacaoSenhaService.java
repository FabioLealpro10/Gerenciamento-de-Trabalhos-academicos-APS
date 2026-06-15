package com.gerenciador.trabalhos.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerenciador.trabalhos.dto.AuthResponse;
import com.gerenciador.trabalhos.dto.EsqueciSenhaResponseDTO;
import com.gerenciador.trabalhos.model.CodigoRecuperacaoSenha;
import com.gerenciador.trabalhos.model.Usuario;
import com.gerenciador.trabalhos.repository.CodigoRecuperacaoSenhaRepository;
import com.gerenciador.trabalhos.repository.UsuarioRepository;
import com.gerenciador.trabalhos.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecuperacaoSenhaService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final CodigoRecuperacaoSenhaRepository codigoRepository;
    private final EmailService emailService;
    private final UsuarioService usuarioService;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.recuperacao-senha.expiracao-minutos:5}")
    private int expiracaoMinutos;

    @Transactional
    public EsqueciSenhaResponseDTO solicitarCodigo(String email) {
        if (email == null || email.isBlank()) {
            return new EsqueciSenhaResponseDTO(false, "Email é obrigatório");
        }

        String emailNormalizado = email.trim();

        if (!usuarioRepository.existsByEmail(emailNormalizado)) {
            return new EsqueciSenhaResponseDTO(false, "Email não cadastrado no sistema");
        }

        codigoRepository.deleteByEmail(emailNormalizado);

        String codigo = gerarCodigo();
        LocalDateTime expiraEm = LocalDateTime.now().plusMinutes(expiracaoMinutos);

        codigoRepository.save(CodigoRecuperacaoSenha.builder()
                .email(emailNormalizado)
                .codigo(codigo)
                .expiraEm(expiraEm)
                .build());

        emailService.enviarCodigoRecuperacao(emailNormalizado, codigo, expiracaoMinutos);

        return new EsqueciSenhaResponseDTO(true, "Enviamos um código para seu email");
    }

    @Transactional
    public AuthResponse verificarCodigo(String email, String codigo) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email é obrigatório");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new RuntimeException("Código é obrigatório");
        }

        String emailNormalizado = email.trim();
        String codigoInformado = codigo.trim();

        CodigoRecuperacaoSenha codigoSalvo = codigoRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RuntimeException("Código não encontrado. Solicite um novo código."));

        if (LocalDateTime.now().isAfter(codigoSalvo.getExpiraEm())) {
            codigoRepository.delete(codigoSalvo);
            throw new RuntimeException("Código expirado. Solicite um novo código.");
        }

        if (!codigoSalvo.getCodigo().equals(codigoInformado)) {
            throw new RuntimeException("Código inválido");
        }

        codigoRepository.delete(codigoSalvo);

        UserDetails userDetails = usuarioService.loadUserByUsername(emailNormalizado);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);
        Usuario usuario = usuarioService.buscarPorEmail(emailNormalizado);

        return new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole());
    }

    private String gerarCodigo() {
        int valor = RANDOM.nextInt(1_000_000);
        return String.format("%06d", valor);
    }
}

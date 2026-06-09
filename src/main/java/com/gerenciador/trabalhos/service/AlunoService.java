package com.gerenciador.trabalhos.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gerenciador.trabalhos.dto.AlunoDTO;
import com.gerenciador.trabalhos.model.Aluno;
import com.gerenciador.trabalhos.repository.AlunoRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    public AlunoService(AlunoRepository alunoRepository, PasswordEncoder passwordEncoder, UsuarioService usuarioService) {
        this.alunoRepository = alunoRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    public AlunoDTO criar(AlunoDTO dto) {
        usuarioService.validarEmailDisponivel(dto.email(), null);

        Aluno aluno = new Aluno();
        aluno.setEmail(dto.email());
        aluno.setNome(dto.nome());
        aluno.setPassword(passwordEncoder.encode(dto.password()));
        aluno.setRole(usuarioService.toDatabaseRole(dto.role() != null ? dto.role() : "ALUNO"));
        aluno.setTurma(dto.turma());

        alunoRepository.save(aluno);

        return converterParaDTO(aluno);
    }

    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public AlunoDTO atualizar(Long id, AlunoDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (dto.nome() != null) {
            aluno.setNome(dto.nome());
        }
        if (dto.email() != null) {
            usuarioService.validarEmailDisponivel(dto.email(), aluno.getId());
            aluno.setEmail(dto.email());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            aluno.setPassword(passwordEncoder.encode(dto.password()));
        }
        if (dto.role() != null) {
            aluno.setRole(usuarioService.toDatabaseRole(dto.role()));
        }
        if (dto.turma() != null) {
            aluno.setTurma(dto.turma());
        }

        alunoRepository.save(aluno);

        return converterParaDTO(aluno);
    }

    public void deletar(Long id) {
        alunoRepository.deleteById(id);
    }

    public AlunoDTO buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    private AlunoDTO converterParaDTO(Aluno aluno) {
        return new AlunoDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getPassword(),
                aluno.getRole(),
                aluno.getTurma());
            
    }
}

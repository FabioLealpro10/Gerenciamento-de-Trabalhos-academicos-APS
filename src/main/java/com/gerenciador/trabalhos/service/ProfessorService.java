package com.gerenciador.trabalhos.service;

<<<<<<< HEAD
import org.springframework.data.domain.PageRequest;
=======
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.dto.ProfessorDTO;

import com.gerenciador.trabalhos.model.Professor;
import com.gerenciador.trabalhos.repository.ProfessorRepository;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    public ProfessorService(ProfessorRepository professorRepository, PasswordEncoder passwordEncoder,
            UsuarioService usuarioService) {
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    public ProfessorDTO criar(ProfessorDTO dto) {
        usuarioService.validarEmailDisponivel(dto.email(), null);

        Professor professor = new Professor();
        professor.setNome(dto.nome());
        professor.setEmail(dto.email());
        professor.setPassword(passwordEncoder.encode(dto.password()));
        professor.setRole(usuarioService.toDatabaseRole(dto.role() != null ? dto.role() : "PROFESSOR"));
        professor.setAreaAtuacao(dto.areaAtuacao());

        professorRepository.save(professor);

        return converterParaDTO(professor);
    }

<<<<<<< HEAD
    public PageResponseDTO<ProfessorDTO> listarTodos(int page, int size) {
        return PageResponseDTO.from(
                professorRepository.findAll(PageRequest.of(page, size))
                        .map(this::converterParaDTO));
    }

    public PageResponseDTO<ProfessorDTO> pesquisarPorNome(String nome, int page, int size) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Informe o parâmetro 'nome' com a subpalavra desejada");
        }

        return PageResponseDTO.from(
                professorRepository.findByNomeContainingIgnoreCase(nome.trim(), PageRequest.of(page, size))
                        .map(this::converterParaDTO));
=======
    public List<ProfessorDTO> listarTodos() {
        return professorRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public PageResponseDTO<ProfessorDTO> listarTodosPaginado(Pageable pageable) {
        Page<Professor> page = professorRepository.findAll(pageable);
        return new PageResponseDTO<>(
                page.getContent().stream().map(this::converterParaDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    }

    public ProfessorDTO atualizar(Long id, ProfessorDTO dto) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        if (dto.nome() != null) {
            professor.setNome(dto.nome());
        }
        if (dto.email() != null) {
            usuarioService.validarEmailDisponivel(dto.email(), professor.getId());
            professor.setEmail(dto.email());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            professor.setPassword(passwordEncoder.encode(dto.password()));
        }
        if (dto.role() != null) {
            professor.setRole(usuarioService.toDatabaseRole(dto.role()));
        }
        if (dto.areaAtuacao() != null) {
            professor.setAreaAtuacao(dto.areaAtuacao());
        }

        professorRepository.save(professor);

        return converterParaDTO(professor);
    }

    public void deletar(Long id) {
        professorRepository.deleteById(id);
    }

    public ProfessorDTO buscarPorId(Long id) {
        return professorRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
    }

    private ProfessorDTO converterParaDTO(Professor professor) {
        return new ProfessorDTO(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                professor.getPassword(),
                professor.getRole(),
                professor.getAreaAtuacao());
    }
}

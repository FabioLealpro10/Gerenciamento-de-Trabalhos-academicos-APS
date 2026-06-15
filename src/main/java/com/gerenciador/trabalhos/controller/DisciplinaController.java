package com.gerenciador.trabalhos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gerenciador.trabalhos.dto.DisciplinaRequestDTO;
import com.gerenciador.trabalhos.dto.DisciplinaResponseDTO;
import com.gerenciador.trabalhos.dto.MatriculaDTO;
import com.gerenciador.trabalhos.dto.MatriculaRequestDTO;
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.Matricula;
import com.gerenciador.trabalhos.repository.MatriculaRepository;
import com.gerenciador.trabalhos.service.DisciplinaService;
import com.gerenciador.trabalhos.service.MatriculaService;

@RestController
@RequestMapping("disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;
    private final MatriculaRepository matriculaRepository;
    private final MatriculaService matriculaService;

    public DisciplinaController(
            DisciplinaService disciplinaService,
            MatriculaRepository matriculaRepository,
            MatriculaService matriculaService) {
        this.disciplinaService = disciplinaService;
        this.matriculaRepository = matriculaRepository;
        this.matriculaService = matriculaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Disciplina> criar(@RequestBody DisciplinaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaService.criar(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<PageResponseDTO<DisciplinaResponseDTO>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(disciplinaService.listarTodas(page, size));
    }

    @GetMapping("/pesquisar")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<PageResponseDTO<DisciplinaResponseDTO>> pesquisarPorNome(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(disciplinaService.pesquisarPorNome(nome, page, size));
    }

    @GetMapping("/aluno/{idAluno}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<List<MatriculaDTO>> listarPorAluno(@PathVariable Long idAluno) {
        List<Matricula> matriculas = matriculaRepository.findByAlunoId(idAluno);

        List<MatriculaDTO> resposta = matriculas.stream()
                .map(m -> new MatriculaDTO(
                        m.getDisciplina().getId(),
                        m.getDisciplina().getNome(),
                        m.getDisciplina().getDataInicio(),
                        m.getDisciplina().getDataFim(),
                        m.getDisciplina().getProfessor().getNome()))
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/professor/{idProfessor}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<PageResponseDTO<DisciplinaResponseDTO>> listarPorProfessor(
            @PathVariable Long idProfessor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(disciplinaService.listarPorProfessor(idProfessor, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<DisciplinaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(disciplinaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<DisciplinaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody DisciplinaRequestDTO dto) {
        return ResponseEntity.ok(disciplinaService.atualizar(id, dto));
    }

    @PostMapping("/matricular")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Matricula> matricularAluno(@RequestBody MatriculaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matriculaService.matricularAluno(dto.alunoId(), dto.disciplinaId()));
    }

    @DeleteMapping("/matricular/aluno/{alunoId}/disciplina/{disciplinaId}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Void> removerMatricula(
            @PathVariable Long alunoId,
            @PathVariable Long disciplinaId) {
        matriculaService.removerMatricula(alunoId, disciplinaId);
        return ResponseEntity.noContent().build();
    }
}

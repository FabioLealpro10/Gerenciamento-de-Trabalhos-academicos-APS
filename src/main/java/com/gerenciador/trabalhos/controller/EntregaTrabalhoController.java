package com.gerenciador.trabalhos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gerenciador.trabalhos.dto.EntregaTrabalhoCorrecaoDTO;
import com.gerenciador.trabalhos.dto.EntregaTrabalhoRequestDTO;
import com.gerenciador.trabalhos.dto.EntregaTrabalhoResponseDTO;
import com.gerenciador.trabalhos.dto.EntregaTrabalhoUpdateDTO;
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.service.EntregaTrabalhoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaTrabalhoController {

    private final EntregaTrabalhoService service;

    @PostMapping
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> entregar(@RequestBody EntregaTrabalhoRequestDTO dto) {
        return ResponseEntity.ok(service.entregar(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<PageResponseDTO<EntregaTrabalhoResponseDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String pesquisa) {
        return ResponseEntity.ok(service.listarTodos(page, size, pesquisa));
    }

    @GetMapping("/trabalho/{trabalhoId}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<PageResponseDTO<EntregaTrabalhoResponseDTO>> listarPorTrabalho(
            @PathVariable Long trabalhoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String pesquisa) {
        return ResponseEntity.ok(service.listarPorTrabalho(trabalhoId, page, size, pesquisa));
    }

    @GetMapping("/aluno/{alunoId}/trabalho/{trabalhoId}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> buscarEntregaAluno(
            @PathVariable Long alunoId,
            @PathVariable Long trabalhoId) {
        return ResponseEntity.ok(service.buscarEntregaAluno(alunoId, trabalhoId));
    }

    @PutMapping("/aluno/{alunoId}/trabalho/{trabalhoId}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> atualizarEntrega(
            @PathVariable Long alunoId,
            @PathVariable Long trabalhoId,
            @RequestBody EntregaTrabalhoUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(trabalhoId, alunoId, dto));
    }

    @PatchMapping("/trabalho/{trabalhoId}/aluno/{alunoId}/corrigir")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> corrigir(
            @PathVariable Long trabalhoId,
            @PathVariable Long alunoId,
            @RequestBody EntregaTrabalhoCorrecaoDTO dto) {
        return ResponseEntity.ok(service.corrigir(trabalhoId, alunoId, dto));
    }
}

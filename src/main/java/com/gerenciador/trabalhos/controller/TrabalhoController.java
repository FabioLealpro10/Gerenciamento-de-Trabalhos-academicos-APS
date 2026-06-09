package com.gerenciador.trabalhos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerenciador.trabalhos.dto.TrabalhoRequestDTO;
import com.gerenciador.trabalhos.dto.TrabalhoResponseDTO;
import com.gerenciador.trabalhos.service.TrabalhoService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trabalhos")
@RequiredArgsConstructor
public class TrabalhoController {

    private final TrabalhoService trabalhoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<TrabalhoResponseDTO> cadastrar(@RequestBody TrabalhoRequestDTO dto) {
        return ResponseEntity.ok(trabalhoService.cadastrar(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<List<TrabalhoResponseDTO>> listar() {
        return ResponseEntity.ok(trabalhoService.listarTodos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<TrabalhoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody TrabalhoRequestDTO dto) {
        return ResponseEntity.ok(trabalhoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        trabalhoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    //  Rota especial: Buscar trabalhos por disciplina
    @GetMapping("/disciplina/{disciplinaId}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<List<TrabalhoResponseDTO>> listarPorDisciplina(@PathVariable Long disciplinaId) {
        return ResponseEntity.ok(trabalhoService.listarPorDisciplina(disciplinaId));
    }
}

package com.gerenciador.trabalhos.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.dto.TrabalhoRequestDTO;
import com.gerenciador.trabalhos.dto.TrabalhoResponseDTO;
import com.gerenciador.trabalhos.service.TrabalhoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trabalhos")
@RequiredArgsConstructor
public class TrabalhoController {

    private final TrabalhoService trabalhoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<TrabalhoResponseDTO> cadastrar(
            @RequestParam String titulo,
            @RequestParam String descricao,
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @RequestParam Long disciplinaId,
            @RequestPart("arquivo") MultipartFile arquivo) {

        TrabalhoRequestDTO dto = new TrabalhoRequestDTO(titulo, descricao, dataInicio, dataFim, disciplinaId);
        return ResponseEntity.ok(trabalhoService.cadastrar(dto, arquivo));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<PageResponseDTO<TrabalhoResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(trabalhoService.listarTodos(page, size));
    }

    @GetMapping("/pesquisar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponseDTO<TrabalhoResponseDTO>> pesquisarPorNome(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(trabalhoService.pesquisarPorNome(nome, page, size));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<TrabalhoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) Long disciplinaId,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {

        TrabalhoRequestDTO dto = new TrabalhoRequestDTO(titulo, descricao, dataInicio, dataFim, disciplinaId);
        return ResponseEntity.ok(trabalhoService.atualizar(id, dto, arquivo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        trabalhoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disciplina/{disciplinaId}")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<PageResponseDTO<TrabalhoResponseDTO>> listarPorDisciplina(
            @PathVariable Long disciplinaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(trabalhoService.listarPorDisciplina(disciplinaId, page, size));
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Resource> baixarPdf(@PathVariable Long id) {
        Resource resource = trabalhoService.baixarPdf(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"trabalho-" + id + ".pdf\"")
                .body(resource);
    }
}

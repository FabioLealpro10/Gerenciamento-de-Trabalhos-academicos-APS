package com.gerenciador.trabalhos.controller;

<<<<<<< HEAD
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
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
<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
=======
import org.springframework.web.bind.annotation.RestController;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce

import com.gerenciador.trabalhos.dto.EntregaTrabalhoCorrecaoDTO;
import com.gerenciador.trabalhos.dto.EntregaTrabalhoRequestDTO;
import com.gerenciador.trabalhos.dto.EntregaTrabalhoResponseDTO;
<<<<<<< HEAD
=======
import com.gerenciador.trabalhos.dto.EntregaTrabalhoUpdateDTO;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.service.EntregaTrabalhoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaTrabalhoController {

    private final EntregaTrabalhoService service;

<<<<<<< HEAD
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> entregar(
            @RequestParam Long trabalhoId,
            @RequestParam Long alunoId,
            @RequestParam String dataEntrega,
            @RequestPart("arquivo") MultipartFile arquivo) {

        EntregaTrabalhoRequestDTO dto = new EntregaTrabalhoRequestDTO(dataEntrega, trabalhoId, alunoId);
        return ResponseEntity.ok(service.entregar(dto, arquivo));
=======
    @PostMapping
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> entregar(@RequestBody EntregaTrabalhoRequestDTO dto) {
        return ResponseEntity.ok(service.entregar(dto));
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
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

<<<<<<< HEAD
    @PutMapping(value = "/aluno/{alunoId}/trabalho/{trabalhoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
=======
    @PutMapping("/aluno/{alunoId}/trabalho/{trabalhoId}")
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> atualizarEntrega(
            @PathVariable Long alunoId,
            @PathVariable Long trabalhoId,
<<<<<<< HEAD
            @RequestParam(required = false) String dataEntrega,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
        return ResponseEntity.ok(service.atualizar(trabalhoId, alunoId, dataEntrega, arquivo));
=======
            @RequestBody EntregaTrabalhoUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(trabalhoId, alunoId, dto));
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    }

    @PatchMapping("/trabalho/{trabalhoId}/aluno/{alunoId}/corrigir")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<EntregaTrabalhoResponseDTO> corrigir(
            @PathVariable Long trabalhoId,
            @PathVariable Long alunoId,
            @RequestBody EntregaTrabalhoCorrecaoDTO dto) {
        return ResponseEntity.ok(service.corrigir(trabalhoId, alunoId, dto));
    }
<<<<<<< HEAD

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Resource> baixarPdf(@PathVariable Long id) {
        Resource resource = service.baixarPdf(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"entrega-" + id + ".pdf\"")
                .body(resource);
    }
=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
}

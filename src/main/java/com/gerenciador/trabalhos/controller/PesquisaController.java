package com.gerenciador.trabalhos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gerenciador.trabalhos.dto.PesquisaResponseDTO;
import com.gerenciador.trabalhos.service.PesquisaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pesquisa")
@RequiredArgsConstructor
public class PesquisaController {

    private final PesquisaService service;

    @GetMapping
    public ResponseEntity<PesquisaResponseDTO> pesquisar(@RequestParam String pesquisa) {
        return ResponseEntity.ok(service.pesquisar(pesquisa));
    }
}

package com.gerenciador.trabalhos.dto;

public record EsqueciSenhaResponseDTO(
        boolean emailCadastrado,
        String mensagem) {
}

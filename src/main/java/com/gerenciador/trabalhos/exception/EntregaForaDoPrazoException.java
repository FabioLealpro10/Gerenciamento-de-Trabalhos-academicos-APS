package com.gerenciador.trabalhos.exception;

import com.gerenciador.trabalhos.dto.EntregaTrabalhoResponseDTO;

import lombok.Getter;

@Getter
public class EntregaForaDoPrazoException extends RuntimeException {

    private final EntregaTrabalhoResponseDTO contexto;

    public EntregaForaDoPrazoException(EntregaTrabalhoResponseDTO contexto) {
        super("Trabalho fora do prazo de envio");
        this.contexto = contexto;
    }
}

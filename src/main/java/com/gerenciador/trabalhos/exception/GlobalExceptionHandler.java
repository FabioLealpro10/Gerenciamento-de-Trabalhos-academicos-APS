package com.gerenciador.trabalhos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.gerenciador.trabalhos.dto.EntregaTrabalhoResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntregaForaDoPrazoException.class)
    public ResponseEntity<EntregaTrabalhoResponseDTO> handleEntregaForaDoPrazo(EntregaForaDoPrazoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getContexto());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErroResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErroResponse("Arquivo PDF excede o limite de 8 MB"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(MensagensExclusao.traduzir(obterCausa(ex))));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntimeException(RuntimeException ex) {
        String mensagem = ex.getMessage();

        if (MensagensExclusao.ehErroDeIntegridade(mensagem)) {
            mensagem = MensagensExclusao.traduzir(mensagem);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(mensagem));
    }

    public record ErroResponse(String mensagem) {
    }

    private String obterCausa(DataIntegrityViolationException ex) {
        if (ex.getMostSpecificCause() != null) {
            return ex.getMostSpecificCause().getMessage();
        }
        return ex.getMessage();
    }
}

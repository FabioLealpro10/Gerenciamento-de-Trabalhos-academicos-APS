package com.gerenciador.trabalhos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
<<<<<<< HEAD
import org.springframework.web.multipart.MaxUploadSizeExceededException;
=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce

import com.gerenciador.trabalhos.dto.EntregaTrabalhoResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntregaForaDoPrazoException.class)
    public ResponseEntity<EntregaTrabalhoResponseDTO> handleEntregaForaDoPrazo(EntregaForaDoPrazoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getContexto());
    }

<<<<<<< HEAD
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErroResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErroResponse("Arquivo PDF excede o limite de 8 MB"));
    }

=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(ex.getMessage()));
    }

    public record ErroResponse(String mensagem) {
    }
}

package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFound(ResourceNotFoundException e) {
        logger.warn("Resource not found - Error: {}", e.getMessage());
        return ResponseEntity.status(404).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 404,
                        "error", "Not Found",
                        "message", e.getMessage()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValid(MethodArgumentNotValidException e) {
        logger.warn("Method Argument Not Valid - Error: {}", e.getMessage());
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (msg1, msg2) -> msg1
                ));
        return ResponseEntity.status(400).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", "Erro de validação nos campos enviados.",
                        "fields", errors

                )
        );
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Object> unrecognizedProperty(UnrecognizedPropertyException e) {
        logger.warn("Unrecognized Property - Error: {}", e.getMessage());
        return ResponseEntity.status(400).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Campo inválido no JSON",
                        "message", "Campo não reconhecido: " + e.getPropertyName(),
                        "path", "/alerts"
                )
        );
    }

    @ExceptionHandler(FailedAlertNotFoundException.class)
    public ResponseEntity<Object> failedAlertNotFound(FailedAlertNotFoundException e) {
        logger.warn("Falha ao reprocessar alerta - Erro: {}", e.getMessage());
        return ResponseEntity.status(404).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 404,
                        "error", "Alerta com falha não encontrado",
                        "message", e.getMessage()
                )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        logger.error("Erro interno inesperado", e);
        return ResponseEntity.status(500).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 500,
                        "error", "Internal Server Error",
                        "message", "Erro interno ao processar a requisição."
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        logger.warn("Erro de argumento inválido: {}", e.getMessage());
        return ResponseEntity.status(400).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "error", "Bad Request",
                "message", e.getMessage()
        ));
    }


}

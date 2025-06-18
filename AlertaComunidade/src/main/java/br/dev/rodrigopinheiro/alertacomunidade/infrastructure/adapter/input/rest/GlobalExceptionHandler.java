package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.SubscriberAlreadyExistsException;
import br.dev.rodrigopinheiro.alertacomunidade.dto.ApiErrorResponse;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        logger.warn("Resource not found - Error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        e.getMessage(),
                        request.getRequestURI(),
                        null,
                        MDC.get("traceId")
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        logger.warn("Payload JSON inválido: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        "JSON malformado ou tipo incompatível.",
                        request.getRequestURI(),
                        null,
                        MDC.get("traceId")
                )
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        logger.warn("Validation failed - Error: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        "Erro de validação nos campos enviados.",
                        request.getRequestURI(),
                        errors,
                        MDC.get("traceId")
                )
        );
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<ApiErrorResponse> unrecognizedProperty(UnrecognizedPropertyException e, HttpServletRequest request) {
        logger.warn("Unrecognized Property - Error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Campo inválido no JSON",
                        "Campo não reconhecido: " + e.getPropertyName(),
                        request.getRequestURI(),
                        null,
                        MDC.get("traceId")
                )
        );
    }

    @ExceptionHandler(FailedAlertNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> failedAlertNotFound(FailedAlertNotFoundException e, HttpServletRequest request) {
        logger.warn("Falha ao reprocessar alerta - Erro: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Alerta com falha não encontrado",
                        e.getMessage(),
                        request.getRequestURI(),
                        null,
                        MDC.get("traceId")
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        logger.warn("Erro de argumento inválido: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        e.getMessage(),
                        request.getRequestURI(),
                        null,
                        MDC.get("traceId")
                )
        );
    }


    @ExceptionHandler(SubscriberAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleSubscriberAlreadyExists(SubscriberAlreadyExistsException e, HttpServletRequest request) {
        logger.warn("Tentativa de registro duplicado: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        "Conflict",
                        e.getMessage(),
                        request.getRequestURI(),
                        null,
                        MDC.get("traceId")
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
        logger.error("Erro interno inesperado", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        "Erro interno ao processar a requisição.",
                        request.getRequestURI(),
                        null,
                        MDC.get("traceId")
                )
        );
    }

}

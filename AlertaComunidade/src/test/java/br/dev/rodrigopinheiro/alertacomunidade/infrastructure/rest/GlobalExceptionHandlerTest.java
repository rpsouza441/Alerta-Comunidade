package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;


import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleResourceNotFoundException() {
        // given
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso não encontrado");

        // when
        ResponseEntity<Object> response = handler.resourceNotFound(ex);
        Map<String, Object> body = castBody(response);


        // then
        assertThat(body.get("status")).isEqualTo(404);
        assertThat(body.get("error")).isEqualTo("Not Found");
        assertThat(body.get("message")).isEqualTo("Recurso não encontrado");
    }

    @Test
    void shouldHandleFailedAlertNotFoundException() {
        // given
        long id = 41L;
        FailedAlertNotFoundException ex = new FailedAlertNotFoundException(id);

        // when
        ResponseEntity<Object> response = handler.failedAlertNotFound(ex);
        Map<String, Object> body = castBody(response);

        // then
        assertThat(body.get("status")).isEqualTo(404);
        assertThat(body.get("error")).isEqualTo("Alerta com falha não encontrado");
        assertThat(body.get("message")).isEqualTo("Alerta com falha ID " + id + " não encontrado");
    }

    @Test
    void shouldHandleUnrecognizedPropertyException() {
        //given
        UnrecognizedPropertyException ex = new UnrecognizedPropertyException(null, "Mensagem", null, String.class, "campoInvalido", null);

        //when
        ResponseEntity<Object> response = handler.unrecognizedProperty(ex);
        Map<String, Object> body = castBody(response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.get("status")).isEqualTo(400);
        assertThat(body.get("error")).isEqualTo("Campo inválido no JSON");
        assertThat(body.get("message")).isEqualTo("Campo não reconhecido: campoInvalido");
        assertThat(body.get("path")).isEqualTo("/alerts");
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // mockando BindingResult com erro de campo
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("alert", "origin", "Origem é obrigatória");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));


        // given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);


        // when
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Erro de validação");
        ResponseEntity<Object> response = handler.methodArgumentNotValid(ex);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Erro de validação");



        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = castBody(response);
        assertThat(body.get("status")).isEqualTo(400);
        assertThat(body.get("error")).isEqualTo("Bad Request");
        assertThat(body.get("message")).isEqualTo("Erro de validação nos campos enviados.");
        Map<String, String> fields = (Map<String, String>) body.get("fields");
        assertThat(fields.get("origin")).isEqualTo("Origem é obrigatória");

    }
    @Test
    void shouldHandleIllegalArgumentException() {
        //given
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        //when
        ResponseEntity<Object> response = handler.handleIllegalArgument(ex);
        Map<String, Object> body = castBody(response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.get("status")).isEqualTo(400);
        assertThat(body.get("error")).isEqualTo("Bad Request");
        assertThat(body.get("message")).isEqualTo("Argumento inválido");
    }

    @Test
    void shouldHandleRuntimeException() {
        //given
        RuntimeException ex = new RuntimeException("Erro interno inesperado");

        //when
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);
        Map<String, Object> body = castBody(response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body.get("status")).isEqualTo(500);
        assertThat(body.get("error")).isEqualTo("Internal Server Error");
        assertThat(body.get("message")).isEqualTo("Erro interno ao processar a requisição.");
    }

    // helper para cast seguro
    @SuppressWarnings("unchecked")
    private Map<String, Object> castBody(ResponseEntity<Object> response) {
        assertThat(response.getBody()).isInstanceOf(Map.class);
        return (Map<String, Object>) response.getBody();
    }
}
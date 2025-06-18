package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;


import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.dto.ApiErrorResponse;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleResourceNotFoundException() {
        // given
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso não encontrado");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/teste");
        // when
        ResponseEntity<ApiErrorResponse> response = handler.resourceNotFound(ex, request);
        ApiErrorResponse body = castBody(response);


        // then
        assertThat(body.status()).isEqualTo(404);
        assertThat(body.error()).isEqualTo("Not Found");
        assertThat(body.message()).isEqualTo("Recurso não encontrado");
    }

    @Test
    void shouldHandleFailedAlertNotFoundException() {
        // given
        long id = 41L;
        FailedAlertNotFoundException ex = new FailedAlertNotFoundException(id);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/alerts");

        // when
        ResponseEntity<ApiErrorResponse> response = handler.failedAlertNotFound(ex, request);
        ApiErrorResponse body = castBody(response);

        // then
        assertThat(body.status()).isEqualTo(404);
        assertThat(body.error()).isEqualTo("Alerta com falha não encontrado");
        assertThat(body.message()).isEqualTo("Alerta com falha ID " + id + " não encontrado");
    }

    @Test
    void shouldHandleUnrecognizedPropertyException() {
        //given
        UnrecognizedPropertyException ex = new UnrecognizedPropertyException(null, "Mensagem", null, String.class, "campoInvalido", null);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/alerts");

        //when
        ResponseEntity<ApiErrorResponse> response = handler.unrecognizedProperty(ex, request);
        ApiErrorResponse body = castBody(response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.status()).isEqualTo(400);
        assertThat(body.error()).isEqualTo("Campo inválido no JSON");
        assertThat(body.message()).isEqualTo("Campo não reconhecido: campoInvalido");
        assertThat(body.path()).isEqualTo("/alerts");
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
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/alerts");
        ResponseEntity<ApiErrorResponse> response = handler.methodArgumentNotValid(ex, request);



        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ApiErrorResponse body = castBody(response);
        assertThat(body.status()).isEqualTo(400);
        assertThat(body.error()).isEqualTo("Bad Request");
        assertThat(body.message()).isEqualTo("Erro de validação nos campos enviados.");
        assertThat(body.fields().get("origin")).isEqualTo("Origem é obrigatória");

    }
    @Test
    void shouldHandleIllegalArgumentException() {
        //given
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/alerts");

        //when
        ResponseEntity<ApiErrorResponse> response = handler.handleIllegalArgument(ex, request);
        ApiErrorResponse body = castBody(response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.status()).isEqualTo(400);
        assertThat(body.error()).isEqualTo("Bad Request");
        assertThat(body.message()).isEqualTo("Argumento inválido");
    }

    @Test
    void shouldHandleRuntimeException() {
        //given
        RuntimeException ex = new RuntimeException("Erro interno inesperado");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/alerts");

        //when
        ResponseEntity<ApiErrorResponse> response = handler.handleGenericException(ex, request);
        ApiErrorResponse body = castBody(response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body.status()).isEqualTo(500);
        assertThat(body.error()).isEqualTo("Internal Server Error");
        assertThat(body.message()).isEqualTo("Erro interno ao processar a requisição.");
    }

    // helper para cast seguro
    private ApiErrorResponse castBody(ResponseEntity<ApiErrorResponse> response) {
        assertThat(response.getBody()).isNotNull();
        return response.getBody();
    }
}

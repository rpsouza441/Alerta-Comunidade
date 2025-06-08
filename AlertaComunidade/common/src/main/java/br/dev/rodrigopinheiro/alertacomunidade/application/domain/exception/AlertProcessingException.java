package br.dev.rodrigopinheiro.alertacomunidade.application.domain.exception;

public class AlertProcessingException extends RuntimeException {
    public AlertProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
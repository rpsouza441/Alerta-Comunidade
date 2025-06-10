package br.dev.rodrigopinheiro.alertacomunidade.common.domain.exception;

public class AlertProcessingException extends RuntimeException {
    public AlertProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
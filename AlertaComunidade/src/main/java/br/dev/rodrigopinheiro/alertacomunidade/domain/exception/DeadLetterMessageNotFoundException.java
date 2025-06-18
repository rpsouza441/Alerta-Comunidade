package br.dev.rodrigopinheiro.alertacomunidade.domain.exception;

public class DeadLetterMessageNotFoundException extends RuntimeException {
    public DeadLetterMessageNotFoundException(Long id) {
        super("Dead letter message ID " + id + " not found");
    }
}

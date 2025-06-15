package br.dev.rodrigopinheiro.alertacomunidade.domain.exception;

public class SubscriberAlreadyExistsException extends RuntimeException {

    public SubscriberAlreadyExistsException(String message) {
        super(message);
    }
}
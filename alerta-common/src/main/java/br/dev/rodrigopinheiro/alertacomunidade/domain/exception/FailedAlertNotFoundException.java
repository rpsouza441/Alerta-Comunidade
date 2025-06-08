package br.dev.rodrigopinheiro.alertacomunidade.domain.exception;


public class FailedAlertNotFoundException extends RuntimeException {
    public FailedAlertNotFoundException(Long id) {
        super("Alerta com falha ID " + id + " não encontrado");
    }
}

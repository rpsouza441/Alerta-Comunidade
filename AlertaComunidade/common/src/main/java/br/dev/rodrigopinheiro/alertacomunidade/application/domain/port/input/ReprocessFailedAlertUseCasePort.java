package br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input;

public interface ReprocessFailedAlertUseCasePort {

    public void execute(Long id);
}

package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

public interface ReprocessFailedAlertUseCasePort {

    public void execute(Long id);
}

package br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input;

public interface ReprocessFailedAlertUseCasePort {

    public void execute(Long id);
}

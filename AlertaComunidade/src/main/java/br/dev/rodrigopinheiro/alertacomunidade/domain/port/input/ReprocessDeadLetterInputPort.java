package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

public interface ReprocessDeadLetterInputPort {
    void execute(Long id);

}

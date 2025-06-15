package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.RegisterSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RegisterSubscriberUseCase implements RegisterSubscriberInputPort {
    private final SubscriberRepositoryPort repository;

    public RegisterSubscriberUseCase(SubscriberRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Subscriber register(Subscriber subscriber) {
        return repository.save(subscriber);
    }
}

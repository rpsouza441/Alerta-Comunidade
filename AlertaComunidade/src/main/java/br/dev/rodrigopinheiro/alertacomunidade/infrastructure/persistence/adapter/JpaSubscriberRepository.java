package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.adapter;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa.SpringDataSubscriberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaSubscriberRepository implements SubscriberRepositoryPort {
    private final SpringDataSubscriberRepository delegate;

    public JpaSubscriberRepository(SpringDataSubscriberRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Subscriber save(Subscriber subscriber) {
        return delegate.save(subscriber);
    }

    @Override
    public List<Subscriber> findAll() {
        return delegate.findAll();
    }
}

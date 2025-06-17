package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.adapter;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa.SpringDataSubscriberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    public boolean existsByEmail(String email) {
        return delegate.existsByEmail(email);
    }

    @Override
    public Optional<Subscriber> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public List<Subscriber> findAll() {
        return delegate.findAll();
    }

    @Override
    public Page<Subscriber> findAll(Pageable pageable) {
        return delegate.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<Subscriber> streamAllActive() {
        return delegate.findAllByActiveIsTrue();
    }
}
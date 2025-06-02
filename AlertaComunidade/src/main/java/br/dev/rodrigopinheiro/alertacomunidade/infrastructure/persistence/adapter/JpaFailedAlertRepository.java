package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.adapter;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa.SpringDataFailedAlertRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaFailedAlertRepository implements FailedAlertRepositoryPort {

    private final SpringDataFailedAlertRepository delegate;

    public JpaFailedAlertRepository(SpringDataFailedAlertRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public FailedAlertNotification save(FailedAlertNotification failed) {
        return delegate.save(failed);
    }

    @Override
    public List<FailedAlertNotification> findAll() {
        return delegate.findAll();
    }

    @Override
    public Optional<FailedAlertNotification> findBydId(Long id) {
        return delegate.findById(id);
    }
}

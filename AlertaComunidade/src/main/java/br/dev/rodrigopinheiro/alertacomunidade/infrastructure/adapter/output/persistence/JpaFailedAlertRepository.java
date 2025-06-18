package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataFailedAlertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<FailedAlertNotification> findAll(Pageable pageable) {
        return delegate.findAll(pageable);
    }

    @Override
    public Optional<FailedAlertNotification> findById(Long id) {
        return delegate.findById(id);
    }
}

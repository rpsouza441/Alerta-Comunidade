package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataAlertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaAlertRepository implements AlertRepositoryPort {

    private final SpringDataAlertRepository delegate;

    public JpaAlertRepository(SpringDataAlertRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public AlertNotification save(AlertNotification alert) {
           return delegate.save(alert);
    }

    @Override
    public Optional<AlertNotification> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public Page<AlertNotification> findAll(Pageable pageable) {
        return delegate.findAll(pageable);
    }

}

package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.adapter;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa.SpringDataFailedAlertRepository;
import org.springframework.stereotype.Component;

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
}

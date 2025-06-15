package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import java.util.List;

public interface SubscriberRepositoryPort {
    Subscriber save(Subscriber subscriber);
    List<Subscriber> findAll();
}

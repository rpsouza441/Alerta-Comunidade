package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubscriberRepositoryPort {
    Subscriber save(Subscriber subscriber);
    boolean existsByEmail(String email);
    Optional<Subscriber> findById(Long id);
    List<Subscriber> findAll();
    Page<Subscriber> findAll(Pageable pageable);

}
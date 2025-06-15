package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSubscriberRepository extends JpaRepository<Subscriber, Long> {

    boolean existsByEmail(String email);

}
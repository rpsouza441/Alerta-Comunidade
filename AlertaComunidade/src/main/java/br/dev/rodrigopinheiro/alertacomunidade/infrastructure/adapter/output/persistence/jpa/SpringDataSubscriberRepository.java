package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface SpringDataSubscriberRepository extends JpaRepository<Subscriber, Long> {

    boolean existsByEmail(String email);

    Stream<Subscriber> findAllByActiveIsTrue();
}
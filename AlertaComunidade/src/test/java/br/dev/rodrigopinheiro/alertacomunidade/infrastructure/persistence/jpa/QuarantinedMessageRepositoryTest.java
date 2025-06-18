package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataQuarantinedMessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class QuarantinedMessageRepositoryTest {

    @Autowired
    private SpringDataQuarantinedMessageRepository repository;

    @Test
    @DisplayName("Deve persistir QuarantinedMessage com sucesso")
    void shouldPersist() {
        QuarantinedMessage message = new QuarantinedMessage();
        message.setQueueName("dlq.test");
        message.setPayload("{}");
        message.setHeaders("{}");

        QuarantinedMessage saved = repository.save(message);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getReceivedAt()).isNotNull();
    }
}
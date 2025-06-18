package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class JpaAlertRepositoryTest {

    @Autowired
    private SpringDataAlertRepository springDataAlertRepository;

    private JpaAlertRepository jpaAlertRepository;

    @BeforeEach
    void setup() {
        jpaAlertRepository = new JpaAlertRepository(springDataAlertRepository);
    }

    @Test
    void shouldSaveAlertNotificationSuccessfully() {
        AlertNotification alert = createValidAlert("Teste completo");
        AlertNotification saved = jpaAlertRepository.save(alert);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull(); // preenchido por @PrePersist
    }

    @Test
    void shouldFindByIdWhenExists() {
        AlertNotification alert = createValidAlert("Buscar por ID");
        AlertNotification saved = jpaAlertRepository.save(alert);

        Optional<AlertNotification> result = jpaAlertRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getMessage()).isEqualTo("Buscar por ID");
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<AlertNotification> result = jpaAlertRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnPagedAlerts() {
        AlertNotification alert1 = createValidAlert("A1");
        jpaAlertRepository.save(alert1);

        AlertNotification alert2 = createValidAlert("A2");
        jpaAlertRepository.save(alert2);

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<AlertNotification> result = jpaAlertRepository.findAll(pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(AlertNotification::getMessage).containsExactly("A1", "A2");
    }

    public static AlertNotification createValidAlert(String message) {
        AlertNotification alert = new AlertNotification();
        alert.setMessage(message);
        alert.setOrigin("INMET");
        alert.setAlertType(AlertType.FIRE);
        alert.setStatus(AlertStatus.RECEIVED);
        return alert;
    }
}

package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.adapter;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa.SpringDataAlertRepository;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa.SpringDataFailedAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class JpaFailedAlertRepositoryTest {

    @Autowired
    private SpringDataFailedAlertRepository failedAlertRepository;

    private JpaFailedAlertRepository jpaFailedAlertRepository;

    @BeforeEach
    void setUp() {
        jpaFailedAlertRepository = new JpaFailedAlertRepository(failedAlertRepository);
    }

    @Test
    void shouldSaveFailedAlertNotificationSuccessfully() {
        FailedAlertNotification failedAlertNotification = createValidFailedAlert("Teste Completo");
        FailedAlertNotification saved = jpaFailedAlertRepository.save(failedAlertNotification);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFailedAt()).isNotNull();
    }

    @Test
    void shouldFindByIdWhenExist(){
        FailedAlertNotification failedAlertNotification = createValidFailedAlert("Buscar por ID");
        FailedAlertNotification saved = jpaFailedAlertRepository.save(failedAlertNotification);

        Optional<FailedAlertNotification> result = failedAlertRepository.findById(saved.getId());

        assertThat(result).isNotNull();
        assertThat(result.get().getMessage()).isEqualTo(saved.getMessage());
    }

    @Test
    void shouldReturnEmptyWhenIDDoesNotExist(){
        Optional<FailedAlertNotification> result = failedAlertRepository.findById(100L);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnPagedFailedAlerts(){
        FailedAlertNotification f1 = createValidFailedAlert("F1");
        failedAlertRepository.save(f1);
        FailedAlertNotification f2 = createValidFailedAlert("F2");
        failedAlertRepository.save(f2);

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<FailedAlertNotification> result = jpaFailedAlertRepository.findAll(pageRequest);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(FailedAlertNotification::getMessage).containsExactly("F1", "F2");
    }

    private FailedAlertNotification createValidFailedAlert(String message) {
        FailedAlertNotification failedAlertNotification = new FailedAlertNotification();
        failedAlertNotification.setOriginalId(99L);
        failedAlertNotification.setMessage(message);
        failedAlertNotification.setOrigin("INMET");
        failedAlertNotification.setAlertType(AlertType.FIRE);
        failedAlertNotification.setCreatedAt(LocalDateTime.now());
        failedAlertNotification.setErrorMessage("Falha ao processar");
        failedAlertNotification.setFailedAt(LocalDateTime.now());
        failedAlertNotification.setStatus(AlertStatus.FAILED);

        return failedAlertNotification;
    }


}

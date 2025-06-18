package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.DeadLetterMessageNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ReprocessDeadLetterInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ReprocessDeadLetterUseCase implements ReprocessDeadLetterInputPort {

    private final DeadLetterRepositoryPort repository;
    private final AlertPublisherPort publisher;
    private final ObjectMapper mapper;

    public ReprocessDeadLetterUseCase(DeadLetterRepositoryPort repository, AlertPublisherPort publisher, ObjectMapper mapper) {
        this.repository = repository;
        this.publisher = publisher;
        this.mapper = mapper;
    }

    @Override
    public void execute(Long id) {
        DeadLetterMessage message = repository.findById(id)
                .orElseThrow(() -> new DeadLetterMessageNotFoundException(id));

        try {
            AlertNotification alert = mapper.readValue(message.getPayload(), AlertNotification.class);
            publisher.publish(alert);
            message.setStatus(DeadLetterStatus.REPROCESSED);
            repository.save(message);
        }catch (Exception e) {
            throw new AlertProcessingException("Erro ao reprocessar mensagem", e);
        }

    }
}

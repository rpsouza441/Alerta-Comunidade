package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;


import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.SubscriberMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.SubscriberAlreadyExistsException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.RegisterSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberRequestDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class RegisterSubscriberUseCase implements RegisterSubscriberInputPort {
    private static final Logger logger = LoggerFactory.getLogger(RegisterSubscriberUseCase.class);

    private final SubscriberRepositoryPort repository;

    public RegisterSubscriberUseCase(SubscriberRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Subscriber register(SubscriberRequestDTO dto) {
        logger.debug("Registrando novo Subscriber: email={}, telefone={}", dto.getEmail(), dto.getPhoneNumber());

        if (repository.existsByEmail(dto.getEmail())) {
            throw new SubscriberAlreadyExistsException("E-mail já cadastrado: " + dto.getEmail());
        }

        try {
            Subscriber entity = SubscriberMapper.toEntity(dto);
            return repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            logger.warn("Violação de integridade ao salvar Subscriber: {}", e.getMessage());
            throw new AlertProcessingException("Falha ao registrar subscriber. Verifique os dados informados.", e);
        }
    }
}
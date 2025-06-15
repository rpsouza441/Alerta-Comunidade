package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.SubscriberMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetSubscriberByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetSubscriberByIdUseCase implements GetSubscriberByIdInputPort {
    private static final Logger logger = LoggerFactory.getLogger(GetSubscriberByIdUseCase.class);

    private final SubscriberRepositoryPort repository;

    public GetSubscriberByIdUseCase(SubscriberRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public SubscriberResponseDTO getById(Long id) {
        logger.info("Executando caso de uso: GetSubscriberById - ID {}", id);
        return repository.findById(id)
                .map(SubscriberMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber com ID: " + id + " não encontrado"));
    }
}
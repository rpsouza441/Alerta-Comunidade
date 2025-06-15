package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;


import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.SubscriberMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllSubscribersInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetAllSubscribersUseCase implements GetAllSubscribersInputPort {
    private static final Logger logger = LoggerFactory.getLogger(GetAllSubscribersUseCase.class);

    private final SubscriberRepositoryPort repository;

    public GetAllSubscribersUseCase(SubscriberRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Page<SubscriberResponseDTO> getAll(Pageable pageable) {
        logger.info("Executando caso de uso: GetAllSubscribers");
        return repository.findAll(pageable)
                .map(SubscriberMapper::toResponse);
    }
}
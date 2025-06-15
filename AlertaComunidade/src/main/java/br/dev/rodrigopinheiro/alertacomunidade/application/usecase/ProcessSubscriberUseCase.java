package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;


import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class ProcessSubscriberUseCase implements ProcessSubscriberInputPort {
    private static final Logger logger = LoggerFactory.getLogger(ProcessSubscriberUseCase.class);

    private final SubscriberRepositoryPort repository;

    public ProcessSubscriberUseCase(SubscriberRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        logger.info("Desativando subscriber {}", id);
        Subscriber subscriber = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber com ID: " + id + " não encontrado"));
        subscriber.setActive(false);
        repository.save(subscriber);
    }

    @Override
    @Transactional
    public void activate(Long id) {
        logger.info("Ativando subscriber {}", id);
        Subscriber subscriber = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber com ID: " + id + " não encontrado"));
        subscriber.setActive(true);
        repository.save(subscriber);
    }
}
package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;


import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.SubscriberMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.RegisterSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/subscribers")
public class SubscriberController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriberController.class);


    private final RegisterSubscriberInputPort registerUseCase;

    public SubscriberController(RegisterSubscriberInputPort registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponseDTO> register(@Valid @RequestBody SubscriberRequestDTO dto){
        logger.debug("Recebida requisição para registrar subscriber: {}", dto);

        Subscriber entity = registerUseCase.register(dto);
        SubscriberResponseDTO response = SubscriberMapper.toResponse(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
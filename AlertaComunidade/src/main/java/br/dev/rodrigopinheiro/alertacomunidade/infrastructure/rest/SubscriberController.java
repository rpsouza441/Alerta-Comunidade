package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.SubscriberMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.RegisterSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {
    private final RegisterSubscriberInputPort registerUseCase;

    public SubscriberController(RegisterSubscriberInputPort registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponseDTO> register(@Valid @RequestBody SubscriberRequestDTO dto){
        var entity = registerUseCase.register(SubscriberMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(SubscriberMapper.toResponse(entity));
    }
}

package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;


import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.SubscriberMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllSubscribersInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetSubscriberByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.RegisterSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/subscribers")
public class SubscriberController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriberController.class);


    private final RegisterSubscriberInputPort registerUseCase;
    private final GetAllSubscribersInputPort getAllUseCase;
    private final GetSubscriberByIdInputPort getByIdUseCase;
    private final ProcessSubscriberInputPort processSubscriberUseCase;
    public SubscriberController(RegisterSubscriberInputPort registerUseCase, GetAllSubscribersInputPort getAllUseCase, GetSubscriberByIdInputPort getByIdUseCase, ProcessSubscriberInputPort processSubscriberUseCase) {
        this.registerUseCase = registerUseCase;
        this.getAllUseCase = getAllUseCase;
        this.getByIdUseCase = getByIdUseCase;
        this.processSubscriberUseCase = processSubscriberUseCase;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponseDTO> register(@Valid @RequestBody SubscriberRequestDTO dto){
        logger.debug("Recebida requisição para registrar subscriber: {}", dto);

        Subscriber entity = registerUseCase.register(dto);
        SubscriberResponseDTO response = SubscriberMapper.toResponse(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public Page<SubscriberResponseDTO> listAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        logger.info("GET /subscribers");
        return getAllUseCase.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponseDTO> getById(@PathVariable Long id) {
        logger.info("GET /subscribers/{}", id);
        SubscriberResponseDTO dto = getByIdUseCase.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        logger.info("POST /subscribers/{}/deactivate", id);
        processSubscriberUseCase.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        logger.info("POST /subscribers/{}/activate", id);
        processSubscriberUseCase.activate(id);
        return ResponseEntity.ok().build();
    }

}
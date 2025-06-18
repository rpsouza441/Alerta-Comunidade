package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.DeadLetterMessageNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllDeadLettersInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ReprocessDeadLetterInputPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dead-letters")
public class DeadLetterController {
    private final GetAllDeadLettersInputPort getAllDeadLettersInputPort;
private final ReprocessDeadLetterInputPort reprocessDeadLetterInputPort;

    public DeadLetterController(GetAllDeadLettersInputPort getAllDeadLettersInputPort,
                                ReprocessDeadLetterInputPort reprocessDeadLetterInputPort) {
        this.getAllDeadLettersInputPort = getAllDeadLettersInputPort;
        this.reprocessDeadLetterInputPort = reprocessDeadLetterInputPort;
    }

    @GetMapping
    public Page<DeadLetterMessage> listAll(
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC)
            Pageable pageable){
        return getAllDeadLettersInputPort.getAll(DeadLetterStatus.PENDING, pageable);
    }

    @GetMapping("/processed")
    public Page<DeadLetterMessage> listProcessed(
            @PageableDefault(
                    page = 0, size = 10, direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return getAllDeadLettersInputPort.getAll(DeadLetterStatus.REPROCESSED, pageable);
    }

    @PostMapping("/{id}/reprocess")
    public ResponseEntity<String> reprocess(@PathVariable Long id){
        reprocessDeadLetterInputPort.execute(id);
        return ResponseEntity.ok("Mensagem reenviada com sucesso para a fila");
    }
}

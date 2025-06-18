package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllQuarantinedMessagesInputPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dlq")
public class QuarantinedMessageController {
    private final GetAllQuarantinedMessagesInputPort getAllQuarantinedMessagesInputPort;


    public QuarantinedMessageController(GetAllQuarantinedMessagesInputPort getAllQuarantinedMessagesInputPort) {
        this.getAllQuarantinedMessagesInputPort = getAllQuarantinedMessagesInputPort;
    }

    @GetMapping
    public Page<QuarantinedMessage> listAll(
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC)
            Pageable pageable){
        return getAllQuarantinedMessagesInputPort.getAll(pageable);

    }
}

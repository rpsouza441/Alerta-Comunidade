package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertRequestDTO;

public interface ProcessAlertInputPort {
    void processAlert(AlertRequestDTO dto);

}

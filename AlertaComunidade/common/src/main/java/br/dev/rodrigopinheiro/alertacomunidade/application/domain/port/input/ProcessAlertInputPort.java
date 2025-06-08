package br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input;


import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertRequestDTO;

public interface ProcessAlertInputPort {
    void processAlert(AlertRequestDTO dto);

}

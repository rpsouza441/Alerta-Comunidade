package br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input;


import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertRequestDTO;

public interface ProcessAlertInputPort {
    void processAlert(AlertRequestDTO dto);

}

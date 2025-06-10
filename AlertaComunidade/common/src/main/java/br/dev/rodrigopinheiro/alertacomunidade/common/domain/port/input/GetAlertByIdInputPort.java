package br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input;


import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertResponseDTO;

public interface GetAlertByIdInputPort {
    AlertResponseDTO getAlertById(Long id);

}

package br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input;


import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertResponseDTO;

public interface GetAlertByIdInputPort {
    AlertResponseDTO getAlertById(Long id);

}

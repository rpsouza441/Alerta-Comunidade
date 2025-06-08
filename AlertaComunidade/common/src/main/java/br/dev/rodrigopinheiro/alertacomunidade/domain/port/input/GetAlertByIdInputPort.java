package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;


import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertResponseDTO;

public interface GetAlertByIdInputPort {
    AlertResponseDTO getAlertById(Long id);

}

package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllAlertsInputPort {
    Page<AlertResponseDTO> getAllAlerts(Pageable pageable);

}

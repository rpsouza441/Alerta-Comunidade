package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertBackupEntryDTO;

import java.util.List;
import java.util.UUID;

public interface BackupStoragePort {
    void save(String errorMessage, AlertNotification alert);
    List<AlertBackupEntryDTO> list();
    void delete(UUID bakcupId);

}

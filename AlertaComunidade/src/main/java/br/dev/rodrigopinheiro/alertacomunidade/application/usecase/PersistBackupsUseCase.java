package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.PersistBackupsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.BackupStoragePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertBackupEntryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PersistBackupsUseCase implements PersistBackupsInputPort {
    private static final Logger logger = LoggerFactory.getLogger(PersistBackupsUseCase.class);

    private final BackupStoragePort backupStoragePort;
    private final FailedAlertRepositoryPort repository;

    public PersistBackupsUseCase(BackupStoragePort backupStoragePort,
                                 FailedAlertRepositoryPort repository) {
        this.backupStoragePort = backupStoragePort;
        this.repository = repository;
    }

    @Override
    public int persistBackups() {
        int processed = 0;
        List<AlertBackupEntryDTO> backupedAlerts = backupStoragePort.list();
        for (AlertBackupEntryDTO dto : backupedAlerts) {
            try {
                repository.save(dto.alert());
                backupStoragePort.delete(dto.backupId());
                processed++;
                logger.info("Backup Alert salvo no banco.");
            } catch (Exception e) {
                logger.error("Erro ao persistir alerta a partir do backup ID {}: {}",
                        dto.backupId(), e.getMessage(), e);
            }
        }
        return processed;
    }
}

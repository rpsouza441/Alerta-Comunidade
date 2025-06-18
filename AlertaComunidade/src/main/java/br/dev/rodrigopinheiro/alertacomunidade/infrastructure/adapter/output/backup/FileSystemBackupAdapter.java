package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.backup;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.BackupStoragePort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertBackupEntryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileSystemBackupAdapter implements BackupStoragePort {
    private static final Logger logger = LoggerFactory.getLogger(FileSystemBackupAdapter.class);
    private static final String BACKUP_DIR = "failed-alerts-backup";

    private final ObjectMapper objectMapper;

    public FileSystemBackupAdapter(
            ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void save(String errorMessage, AlertNotification alert) {
        try {
            FailedAlertNotification failed = FailedAlertMapper.from(alert, errorMessage);

            Files.createDirectories(Paths.get(BACKUP_DIR));
            UUID uuid = UUID.randomUUID();
            Path file = Paths.get(BACKUP_DIR, uuid + ".json");
            String json = objectMapper.writeValueAsString(failed);
            Files.writeString(file, json, StandardOpenOption.CREATE_NEW);
            logger.info("Alerta salvo em disco");
        } catch (IOException e) {
            logger.error("Não foi possível gravar alerta em arquivo de backup", e);
        }
    }

    @Override
    public List<AlertBackupEntryDTO> list() {
        List<AlertBackupEntryDTO> alerts = new ArrayList<>();
        Path dir = Paths.get(BACKUP_DIR);
        if (!Files.exists(dir)) {
            return alerts;
        }
        try (Stream<Path> files = Files.list(dir)) {
            files.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String filename = path.getFileName().toString();
                            if (filename.endsWith(".json")) {
                                UUID uuid = UUID.fromString(filename.replace(".json", ""));
                                String json = Files.readString(path);
                                FailedAlertNotification alert = objectMapper.readValue(json, FailedAlertNotification.class);
                                alerts.add(new AlertBackupEntryDTO(uuid, alert));
                            }

                        } catch (IOException e) {
                            logger.error("Erro ao ler arquivo de backup {}", path, e);
                        }
                    });
        } catch (IOException e) {
            logger.error("Erro ao listar backups", e);
        }
        return alerts;
    }

    @Override
    public void delete(UUID backupId) {
        Path path = Paths.get(BACKUP_DIR, backupId + ".json");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("Erro ao deletar arquivo de backup com ID {}", backupId, e);
        }

    }
}

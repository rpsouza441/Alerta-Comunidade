package br.dev.rodrigopinheiro.alertacomunidade.dto;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;

import java.util.UUID;

public record AlertBackupEntryDTO(UUID backupId, FailedAlertNotification alert) {}


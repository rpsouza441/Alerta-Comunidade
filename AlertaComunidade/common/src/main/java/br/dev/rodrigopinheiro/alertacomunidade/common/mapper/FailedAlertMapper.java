package br.dev.rodrigopinheiro.alertacomunidade.common.mapper;


import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.FailedAlertNotification;

public class FailedAlertMapper {

    public static FailedAlertNotification from(AlertNotification alert, String errorMessage) {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setOriginalId(alert.getId());
        failed.setMessage(alert.getMessage());
        failed.setOrigin(alert.getOrigin());
        failed.setAlertType(alert.getAlertType());
        failed.setStatus(AlertStatus.FAILED);
        failed.setErrorMessage(errorMessage);
        failed.setCreatedAt(alert.getCreatedAt());
        return failed;
    }

    public static AlertNotification toAlertNotification(FailedAlertNotification failed) {
        AlertNotification alert = new AlertNotification();
        if (failed.getOriginalId() != null) alert.setId(failed.getOriginalId());
        alert.setMessage(failed.getMessage());
        alert.setOrigin(failed.getOrigin());
        alert.setAlertType(failed.getAlertType());
        alert.setStatus(AlertStatus.RECEIVED);
        alert.setCreatedAt(failed.getCreatedAt());
        return alert;
    }
}
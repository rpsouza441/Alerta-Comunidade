package br.dev.rodrigopinheiro.alertacomunidade.application.mapper;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;

public class FailedAlertMapper {

    public static FailedAlertNotification from(AlertNotification alert, String errorMessage) {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setMessage(alert.getMessage());
        failed.setOrigin(alert.getOrigin());
        failed.setAlertType(alert.getAlertType());
        failed.setStatus(alert.getStatus());
        failed.setErrorMessage(errorMessage);
        return failed;
    }
}
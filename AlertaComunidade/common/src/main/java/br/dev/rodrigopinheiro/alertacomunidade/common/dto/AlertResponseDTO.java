package br.dev.rodrigopinheiro.alertacomunidade.common.dto;

import br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType;

import java.time.LocalDateTime;

public class AlertResponseDTO {

    private Long id;
    private String message;
    private String origin;
    private AlertType alertType;
    private AlertStatus alertStatus;
    private LocalDateTime createdAt;

    public AlertResponseDTO() {
    }

    public AlertResponseDTO(Long id, String message, String origin, AlertType alertType, AlertStatus alertStatus, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.origin = origin;
        this.alertType = alertType;
        this.alertStatus = alertStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public AlertStatus getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(AlertStatus alertStatus) {
        this.alertStatus = alertStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

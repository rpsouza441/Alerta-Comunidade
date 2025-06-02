package br.dev.rodrigopinheiro.alertacomunidade.domain.model;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_alert_notifications")
public class FailedAlertNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_id")
    private Long originalId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "error_message", nullable = false, length = 1000)
    private String errorMessage;

    @Column(name = "failed_at", nullable = false, updatable = false)
    private LocalDateTime failedAt = LocalDateTime.now();

    @Column(name = "reprocessed_at")
    private LocalDateTime reprocessedAt;


    public FailedAlertNotification() {
    }

    public FailedAlertNotification(Long id, Long originalId, String message, String origin, AlertType alertType, LocalDateTime createdAt, AlertStatus status, String errorMessage, LocalDateTime failedAt, LocalDateTime reprocessedAt) {
        this.id = id;
        this.originalId = originalId;
        this.message = message;
        this.origin = origin;
        this.alertType = alertType;
        this.createdAt = createdAt;
        this.status = status;
        this.errorMessage = errorMessage;
        this.failedAt = failedAt;
        this.reprocessedAt = reprocessedAt;
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

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(LocalDateTime failedAt) {
        this.failedAt = failedAt;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public LocalDateTime getReprocessedAt() {
        return reprocessedAt;
    }

    public void setReprocessedAt(LocalDateTime reprocessedAt) {
        this.reprocessedAt = reprocessedAt;
    }
}

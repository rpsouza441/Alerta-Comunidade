package br.dev.rodrigopinheiro.alertacomunidade.common.domain.model;


import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "alert_notifications")
public class AlertNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "message", nullable = false)
    private String message;

    @NotBlank
    @Column(name = "origin", nullable = false)
    private String origin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public AlertNotification() {
    }

    public AlertNotification(Long id, String message, String origin, AlertType alertType, LocalDateTime createdAt, AlertStatus status) {
        this.id = id;
        this.message = message;
        this.origin = origin;
        this.alertType = alertType;
        this.createdAt = createdAt;
        this.status = status;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }
}

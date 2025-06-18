package br.dev.rodrigopinheiro.alertacomunidade.domain.model;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "dead_letter_messages")
public class DeadLetterMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "queue_name", nullable = false)
    private String queueName;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Lob
    @Column(name = "headers", nullable = false)
    private String headers;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeadLetterStatus status;

    @Column(name = "received_at", nullable = false, updatable = false)
    private LocalDateTime receivedAt;

    public DeadLetterMessage() {
    }


    @PrePersist
    public void prePersist() {
        this.receivedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = DeadLetterStatus.PENDING;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public DeadLetterStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull DeadLetterStatus status) {
        this.status = status;
    }
}

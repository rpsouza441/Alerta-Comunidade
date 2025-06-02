package br.dev.rodrigopinheiro.alertacomunidade.domain.enums;

/**
 * Representa os possíveis estados de um alerta dentro do fluxo assíncrono do sistema.
 */
public enum AlertStatus {
    RECEIVED,         // Alerta recebido via API REST
    SENT_TO_QUEUE,    // Alerta publicado com sucesso no RabbitMQ
    DELIVERED,        // (Reservado para quando for confirmado o consumo ou entrega final)
    FAILED,            // (Reservado para falhas de publicação ou envio)
    REPROCESSED         // Reenviado com sucesso após falha
}

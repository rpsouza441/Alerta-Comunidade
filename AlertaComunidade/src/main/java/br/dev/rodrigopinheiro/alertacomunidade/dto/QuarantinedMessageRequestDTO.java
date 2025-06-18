package br.dev.rodrigopinheiro.alertacomunidade.dto;

public record QuarantinedMessageRequestDTO (
        String queueName,
        String paylod,
        String headers
){
}

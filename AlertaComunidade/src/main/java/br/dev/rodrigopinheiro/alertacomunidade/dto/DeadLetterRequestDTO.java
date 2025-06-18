package br.dev.rodrigopinheiro.alertacomunidade.dto;

public record DeadLetterRequestDTO (
        String queueName,
        String paylod,
        String headers
){
}

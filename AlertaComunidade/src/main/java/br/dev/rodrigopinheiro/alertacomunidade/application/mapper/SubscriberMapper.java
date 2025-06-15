package br.dev.rodrigopinheiro.alertacomunidade.application.mapper;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;

public class SubscriberMapper {
    public static Subscriber toEntity(SubscriberRequestDTO dto) {
        Subscriber s = new Subscriber();
        s.setEmail(dto.getEmail());
        s.setPhoneNumber(dto.getPhoneNumber());
        s.setActive(true);
        return s;
    }

    public static SubscriberResponseDTO toResponse(Subscriber entity) {
        return new SubscriberResponseDTO(entity.getId(), entity.getEmail(), entity.getPhoneNumber(), entity.isActive());
    }
}
package br.dev.rodrigpinheiro.AlertaComunidade.dto;

import br.dev.rodrigpinheiro.AlertaComunidade.enums.AlertStatus;
import br.dev.rodrigpinheiro.AlertaComunidade.enums.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AlertRequestDTO {

    @NotBlank(message = "A mensagem do alerta é obrigatória.")
    private String message;

    @NotBlank(message = "A origem do alerta é obrigatória.")
    private String origin;

    @NotNull(message = "O tipo de alerta é obrigatório.")
    private AlertType alertType;



    public AlertRequestDTO() {
    }


    public AlertRequestDTO(String message, String origin, AlertType alertType) {
        this.message = message;
        this.origin = origin;
        this.alertType = alertType;
    }

    public @NotBlank String getMessage() {
        return message;
    }

    public void setMessage(@NotBlank String message) {
        this.message = message;
    }

    public @NotBlank String getOrigin() {
        return origin;
    }

    public void setOrigin(@NotBlank String origin) {
        this.origin = origin;
    }

    public @NotNull(message = "O tipo de alerta é obrigatório.") AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(@NotNull(message = "O tipo de alerta é obrigatório.") AlertType alertType) {
        this.alertType = alertType;
    }

}

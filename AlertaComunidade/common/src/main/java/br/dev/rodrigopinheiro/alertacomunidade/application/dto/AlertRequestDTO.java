package br.dev.rodrigopinheiro.alertacomunidade.application.dto;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.enums.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class AlertRequestDTO {

    @Size(min = 1, max = 50, message = "A mensagem do alerta deve ter entre 1 e 50 caracteres.")
    @NotBlank(message = "A mensagem do alerta é obrigatória.")
    private String message;

    @Pattern(regexp = "^[A-Z]{4,10}$", message = "Deve conter apenas letras maiúsculas entre 4 e 10 caracteres.")
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

}

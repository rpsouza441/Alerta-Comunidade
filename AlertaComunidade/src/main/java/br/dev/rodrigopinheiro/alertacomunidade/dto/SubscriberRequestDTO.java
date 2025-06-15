package br.dev.rodrigopinheiro.alertacomunidade.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class SubscriberRequestDTO {
    @Email(message = "Email inválido")
    private String email;

    @Pattern(regexp = "^\+?\d{10,15}$", message = "Telefone inválido")
    private String phoneNumber;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}

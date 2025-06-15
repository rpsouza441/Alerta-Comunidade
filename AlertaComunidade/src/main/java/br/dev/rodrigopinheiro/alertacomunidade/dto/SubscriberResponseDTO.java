package br.dev.rodrigopinheiro.alertacomunidade.dto;


public class SubscriberResponseDTO {
    private Long id;
    private String email;
    private String phoneNumber;
    private boolean active;
    public SubscriberResponseDTO(Long id, String email, String phoneNumber, boolean active) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
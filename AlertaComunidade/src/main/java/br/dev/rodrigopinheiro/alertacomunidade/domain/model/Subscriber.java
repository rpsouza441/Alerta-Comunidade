package br.dev.rodrigopinheiro.alertacomunidade.domain.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(name = "email")
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Subscriber() {
    }

    public Subscriber(Long id, String email, String phoneNumber, boolean active) {
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
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
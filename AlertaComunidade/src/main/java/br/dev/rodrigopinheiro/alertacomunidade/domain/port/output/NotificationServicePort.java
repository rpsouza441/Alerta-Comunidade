package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

public interface NotificationServicePort {
    void sendEmail(String to, String subject, String body);
    void sendSms(String to, String message);
}

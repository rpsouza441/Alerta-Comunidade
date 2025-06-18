package br.dev.rodrigopinheiro.alertacomunidade.application.service;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.NotificationServicePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import com.twilio.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class SubscriberNotifier {

    private static final Logger logger = LoggerFactory.getLogger(SubscriberNotifier.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");

    private final SubscriberRepositoryPort subscriberRepository;
    private final NotificationServicePort notificationService;

    public SubscriberNotifier(SubscriberRepositoryPort subscriberRepository, NotificationServicePort notificationService) {
        this.subscriberRepository = subscriberRepository;
        this.notificationService = notificationService;
    }

    public void notifySubscribers(AlertNotification alert) {
        String formattedMessage = "Instituto "+ alert.getOrigin() + " Alerta: " + alert.getMessage();
        String subject = "Alerta: " + alert.getAlertType();


        try (Stream<Subscriber> subscribers = subscriberRepository.streamAllActive()){
            subscribers.forEach(s -> {
                String email = s.getEmail();
                String phone = s.getPhoneNumber();

                if (isValidEmail(email)) {
                    try {
                        notificationService.sendEmail(email, subject, formattedMessage);
                    } catch (MailException e) {
                        logger.error("Falha ao enviar email para {}: {}", email, e.getMessage());
                    }
                } else {
                    logger.warn("Email ausente ou inválido para subscriber ID={}", s.getId());
                }

                if (isValidPhone(phone)) {
                    try {
                        notificationService.sendSms(phone, formattedMessage);
                    } catch (ApiException e) {
                        logger.error("Falha ao enviar sms para {}: {}", phone, e.getMessage());
                    }
                } else {
                    logger.warn("Telefone ausente ou inválido para subscriber ID={}", s.getId());
                }
            });
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
}
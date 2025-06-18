package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.notification;


import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.NotificationServicePort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements NotificationServicePort {
    private final JavaMailSender mailSender;
    private final String fromNumber;

    public NotificationService(JavaMailSender mailSender,
                               @Value("${twilio.account-sid}") String sid,
                               @Value("${twilio.auth-token}") String token,
                               @Value("${twilio.from-number}") String fromNumber) {
        this.mailSender = mailSender;
        this.fromNumber = fromNumber;
        Twilio.init(sid, token);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }

    @Override
    public void sendSms(String to, String message) {
        Message.creator(new PhoneNumber(to), new PhoneNumber(fromNumber), message).create();
    }
}
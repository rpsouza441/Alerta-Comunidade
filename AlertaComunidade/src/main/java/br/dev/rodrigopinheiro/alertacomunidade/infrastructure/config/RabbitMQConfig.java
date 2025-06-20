package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // === Exchange única para todos os tipos de alerta ===
    public static final String EXCHANGE = "alerts.exchange";

    // === Filas e Routing Keys ===
    public static final String CRITICAL_QUEUE = "alerts.critical.queue";
    public static final String NORMAL_QUEUE = "alerts.normal.queue";
    public static final String LOG_QUEUE = "alerts.log.queue";

    public static final String CRITICAL_ROUTING_KEY = "alerts.critical";
    public static final String NORMAL_ROUTING_KEY = "alerts.normal";
    public static final String LOG_ROUTING_KEY = "alerts.log";

    public static final String DEAD_LETTER_EXCHANGE = "dead.exchange";
    public static final String DEAD_LETTER_ROUTING_KEY_CRITICAL = "dead.critical";
    public static final String DEAD_LETTER_ROUTING_KEY_NORMAL = "dead.normal";
    public static final String DEAD_CRITICAL_QUEUE = "dead.critical.queue";
    public static final String DEAD_NORMAL_QUEUE = "dead.normal.queue";


    @Bean
    public DirectExchange alertsExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // === FILA com suporte a prioridade ===
    @Bean
    public Queue criticalQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10); // Prioridade de 0 a 10
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY_CRITICAL);
        return new Queue(CRITICAL_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue criticalDlq() {
        return new Queue(DEAD_CRITICAL_QUEUE, true, false, false);
    }

    @Bean
    public Queue normalQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 5); // Prioridade de 0 a 10
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY_NORMAL);
        return new Queue(NORMAL_QUEUE, true,false, false,args);
    }

    @Bean
    public Queue normalDlq() {
        return new Queue(DEAD_NORMAL_QUEUE, true, false, false);
    }

    @Bean
    public Queue logQueue() {
        return new Queue(LOG_QUEUE, true);
    }

    // === Bindings ===
    @Bean
    public Binding criticalBinding() {
        return BindingBuilder.bind(criticalQueue())
                .to(alertsExchange())
                .with(CRITICAL_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterCriticalBinding() {
        return BindingBuilder.bind(criticalDlq())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY_CRITICAL);
    }

    @Bean
    public Binding normalBinding() {
        return BindingBuilder.bind(normalQueue())
                .to(alertsExchange())
                .with(NORMAL_ROUTING_KEY);
    }
    @Bean
    public Binding deadLetterNormalBinding() {
        return BindingBuilder.bind(normalDlq())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY_NORMAL);
    }

    @Bean
    public Binding logBinding() {
        return BindingBuilder.bind(logQueue())
                .to(alertsExchange())
                .with(LOG_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJackson() {
        return builder -> {
            builder.featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            builder.modules(new JavaTimeModule());
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }

    // Template de envio de mensagens
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}


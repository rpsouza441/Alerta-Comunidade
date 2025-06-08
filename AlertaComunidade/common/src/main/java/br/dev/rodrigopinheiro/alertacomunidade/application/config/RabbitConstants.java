package br.dev.rodrigopinheiro.alertacomunidade.application.config;


public final class RabbitConstants {
    private RabbitConstants(){}

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
}
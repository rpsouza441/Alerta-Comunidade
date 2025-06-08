package br.dev.rodrigopinheiro.alertacomunidade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class ApiProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiProducerApplication.class, args);
    }

}

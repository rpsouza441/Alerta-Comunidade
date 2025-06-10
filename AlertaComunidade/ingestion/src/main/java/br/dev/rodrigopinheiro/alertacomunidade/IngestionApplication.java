package br.dev.rodrigopinheiro.alertacomunidade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
		scanBasePackages = "br.dev.rodrigopinheiro.alertacomunidade.common",
		exclude = {
				DataSourceAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class
		}
)

public class IngestionApplication  {

	public static void main(String[] args) {
		SpringApplication.run(IngestionApplication .class, args);
	}

}

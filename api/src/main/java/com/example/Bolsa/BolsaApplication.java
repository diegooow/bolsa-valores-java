package com.example.Bolsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;

@SpringBootApplication
public class BolsaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BolsaApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.sqlite.JDBC");
		String dbUrl = System.getenv("DATABASE_URL");
		if (dbUrl == null || dbUrl.isEmpty()) {
			dbUrl = "jdbc:sqlite:src/main/resources/banco.db";
		}
		dataSource.setUrl(dbUrl);
		dataSource.setUsername("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}

	@Bean
	public RabbitMQWorker rabbitMQWorker() {
		return new RabbitMQWorker();
	}

}

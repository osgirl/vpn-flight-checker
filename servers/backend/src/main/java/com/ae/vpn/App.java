package com.ae.vpn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration

@EntityScan({"com.ae.vpn.servers.common.model"})
@ComponentScan({"com.ae.vpn.controller", "com.ae.vpn.servers.common.docker", "com.ae.vpn.service"})
@EnableJpaRepositories({"com.ae.vpn.repositories"})
@EnableScheduling
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}

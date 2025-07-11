package com.cmcabrera.cardcostapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.cmcabrera.cardcostapi.repository")
@EntityScan("com.cmcabrera.cardcostapi.entity")
public class CardCostApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardCostApiApplication.class, args);
    }

}

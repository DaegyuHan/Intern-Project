package com.han.internproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InternProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternProjectApplication.class, args);
    }

}

package com.finovoria.finastrax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.finovoria.finastrax.entity"
})
@EnableJpaRepositories(basePackages = {
    "com.finovoria.finastrax.repository"
})
public class FinastraxApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinastraxApplication.class, args);
    }
}
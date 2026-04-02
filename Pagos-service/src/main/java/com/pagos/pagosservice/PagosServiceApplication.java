package com.pagos.pagosservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.pagos.pagosservice.clients")
@EnableScheduling
public class PagosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagosServiceApplication.class, args);
    }

}

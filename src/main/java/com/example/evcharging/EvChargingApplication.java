package com.example.evcharging;

import com.example.evcharging.config.TariffProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TariffProperties.class)
public class EvChargingApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvChargingApplication.class, args);
    }
}

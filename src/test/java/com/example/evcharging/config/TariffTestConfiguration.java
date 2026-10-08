package com.example.evcharging.config;

import com.example.evcharging.service.BillingService;
import com.example.evcharging.strategy.AcTariffStrategy;
import com.example.evcharging.strategy.DcTariffStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TariffProperties.class)
@Import({AcTariffStrategy.class, DcTariffStrategy.class, BillingService.class})
public class TariffTestConfiguration {
}

package com.trainticketbooking.app.Configs;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafLayoutConfig {
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
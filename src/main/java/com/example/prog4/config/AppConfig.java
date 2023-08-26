package com.example.prog4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CompanyConf companyConf() {
        return new CompanyConf();
    }
}

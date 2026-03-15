package ru.tbank.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppСonfig {

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}

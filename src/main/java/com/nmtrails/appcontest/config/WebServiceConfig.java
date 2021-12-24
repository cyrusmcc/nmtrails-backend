package com.nmtrails.appcontest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebServiceConfig {

    @Bean
    public WebClient apiClient() {
        String host = "https://www.googleapis.com/customsearch/v1";
        return WebClient.create(host);
    }
}

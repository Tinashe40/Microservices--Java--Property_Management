package com.proveritus.cloudutility.client.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientBuilder {

    private final WebClient.Builder webClientBuilder;

    public WebClientBuilder(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public WebClient build() {
        return webClientBuilder.build();
    }
}
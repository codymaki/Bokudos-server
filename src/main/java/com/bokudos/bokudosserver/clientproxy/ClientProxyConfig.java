package com.bokudos.bokudosserver.clientproxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientProxyConfig {

//    @Value("${users.api.url.v1}")
    private String usersEndpointUrl = "http://localhost:8082/api/v1/games";

    @Bean
    public GamesControllerV1 getUserResourceV1() {
        WebClient webClient = WebClient.builder()
                .baseUrl(usersEndpointUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
        return null;
//        webClient
//
//        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
//        ResteasyWebTarget target = client.target(usersEndpointUrl);
//        return target.proxy(GamesControllerV1.class);
    }

}

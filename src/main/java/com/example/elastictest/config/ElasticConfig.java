package com.example.elastictest.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class ElasticConfig {

    @Bean
    RestHighLevelClient client() {

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("311080b962754ade9b00fd2f758ab967.europe-west3.gcp.cloud.es.io:9243")
                .usingSsl()
                .withBasicAuth("elastic", "W5W3GXQXQDjt4HdFKTWyvJ2M")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}

package com.example.elastictest;

import com.example.elastictest.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
@EnableConfigurationProperties(StorageProperties.class)
public class ElastictestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElastictestApplication.class, args);
    }

}

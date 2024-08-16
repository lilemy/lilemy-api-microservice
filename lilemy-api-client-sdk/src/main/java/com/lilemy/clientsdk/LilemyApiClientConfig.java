package com.lilemy.clientsdk;

import com.lilemy.clientsdk.client.LilemyApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan
@ConfigurationProperties("lilemy.client")
public class LilemyApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public LilemyApiClient lilemyApiClient() {
        return new LilemyApiClient(accessKey, secretKey);
    }
}

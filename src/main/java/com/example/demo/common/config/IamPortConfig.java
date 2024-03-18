package com.example.demo.common.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamPortConfig {
    @Value("${imp.api-key}")
    private String restApiKey;

    @Value("${imp.secret-key}")
    private String restApiSecret;


    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(restApiKey, restApiSecret);
    }
}

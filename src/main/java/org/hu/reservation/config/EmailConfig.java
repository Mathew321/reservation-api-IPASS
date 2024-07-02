package org.hu.reservation.config;

import com.azure.communication.email.EmailAsyncClient;
import com.azure.communication.email.EmailClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Value("${azure.communication.service.api-key}")
    private String accessKey;

    @Value("${azure.communication.service.endpoint}")
    private String endpoint;

    @Bean
    public EmailAsyncClient emailAsyncClient(){
        String connectionString = endpoint + accessKey;
        return new EmailClientBuilder()
                .connectionString(connectionString)
                .buildAsyncClient();
    }

}

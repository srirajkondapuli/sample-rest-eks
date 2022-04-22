package com.myown.app.sample.configuration;


import com.myown.app.sample.service.CredentialProvider;
import com.myown.app.sample.service.DefaultCredentialProvider;
import com.myown.app.sample.service.EncryptDecryptProvider;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.myown.app.sample"})
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @RefreshScope
    @Bean(name="credentialProvider")
    public CredentialProvider defaultCredentialProvider() {
        return new DefaultCredentialProvider();
    }

    @Bean(name="encryptionProvider")
    public EncryptDecryptProvider encryptionProvider() {
        return new EncryptDecryptProvider();
    }



}


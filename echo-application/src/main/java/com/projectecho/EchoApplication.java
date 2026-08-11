package com.projectecho;

import com.projectecho.identity.infrastructure.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(JwtProperties.class)
public class EchoApplication {
    public static void main(final String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }
}

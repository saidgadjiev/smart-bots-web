package ru.gadjini.telegram.smart.bots.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(WebApplication.class, args);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}

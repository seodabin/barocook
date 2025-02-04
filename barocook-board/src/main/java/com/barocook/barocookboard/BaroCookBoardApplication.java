package com.barocook.barocookboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BaroCookBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaroCookBoardApplication.class, args);
    }

}

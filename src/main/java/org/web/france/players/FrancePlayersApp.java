package org.web.france.players;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class FrancePlayersApp extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(FrancePlayersApp.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(FrancePlayersApp.class, args);
    }
}

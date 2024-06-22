package com.example.Bolsa.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class WebConfiguration {

    @Bean
    public CorsConfigurationSource corsConfigurerSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //Quando hospedar, mudar string abaixo
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081","/**"));
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST","PUT", "DELETE", "OPTIONS", "HEAD"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
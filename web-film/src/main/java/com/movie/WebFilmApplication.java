package com.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
public class WebFilmApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFilmApplication.class, args);
    }
}

  package com.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

  @SpringBootApplication(scanBasePackages = "com.es.*")
  public class AppRunner extends SpringBootServletInitializer {

      @Override
      protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
          return builder.sources(AppRunner.class);
      }

      public static void main(String[] args) {
          SpringApplication.run(AppRunner.class);
      }
  }

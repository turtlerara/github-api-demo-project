package com.demo.app.config;

import com.demo.app.annotation.ExcludeFromJacocoGeneratedReport;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ExcludeFromJacocoGeneratedReport
public class SwaggerConfig {

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI().info(new Info().title("Demo App API"));
  }
}

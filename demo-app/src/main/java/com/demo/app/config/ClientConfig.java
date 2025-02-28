package com.demo.app.config;

import com.demo.app.annotation.ExcludeFromJacocoGeneratedReport;
import com.demo.client.github.GitHubApiClientFactory;
import com.demo.client.github.GitHubUserClient;
import com.demo.client.github.config.GitHubApiClientConfig;
import com.demo.client.github.config.GitHubApiVersion;
import com.demo.client.github.impl.GitHubApiClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ExcludeFromJacocoGeneratedReport
public class ClientConfig {

  @Bean
  public GitHubApiClientFactory gitHubApiClientFactory() {
    return new GitHubApiClientFactoryImpl(
        GitHubApiClientConfig.builder()
            .apiVersion(GitHubApiVersion.V_2022_11_28)
            .userAgent("Demo-App")
            .build());
  }

  @Bean
  public GitHubUserClient gitHubUserClient() {
    return gitHubApiClientFactory().userClient();
  }
}

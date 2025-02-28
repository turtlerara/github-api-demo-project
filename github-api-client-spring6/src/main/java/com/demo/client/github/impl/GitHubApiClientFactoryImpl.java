package com.demo.client.github.impl;

import com.demo.client.github.GitHubApiClientFactory;
import com.demo.client.github.GitHubUserClient;
import com.demo.client.github.config.GitHubApiClientConfig;
import com.demo.client.github.spring.RestClientFactory;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.client.RestClient;

@NullMarked
public class GitHubApiClientFactoryImpl implements GitHubApiClientFactory {

  private final RestClient restClient;

  /**
   * Constructs a {@link GitHubApiClientFactoryImpl} instance using the provided client
   * configuration.
   *
   * @param clientConfig client configuration
   */
  public GitHubApiClientFactoryImpl(GitHubApiClientConfig clientConfig) {
    this.restClient = new RestClientFactory(clientConfig).restClient();
  }

  /** {@inheritDoc} */
  @Override
  public GitHubUserClient userClient() {
    return new GitHubUserClientImpl(restClient);
  }
}

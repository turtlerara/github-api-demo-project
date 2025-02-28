package com.demo.client.github;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GitHubApiClientFactory {

  /**
   * Builds a new {@link GitHubUserClient} instance.
   *
   * @return github user client
   */
  GitHubUserClient userClient();
}

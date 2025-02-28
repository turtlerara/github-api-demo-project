package com.demo.client.github.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class GitHubApiClientConfig {

  /** GitHub API version to use for requests. */
  @NonNull @Builder.Default GitHubApiVersion apiVersion = GitHubApiVersion.LATEST;

  /** User-Agent header value to use for GitHub API requests. */
  @NonNull @Builder.Default String userAgent = "GitHub-Api-Client-App";
}

package com.demo.client.github.exception;

import lombok.Getter;

@Getter
public class GitHubApiUserNotFoundException extends GitHubApiResourceNotFoundException {

  public GitHubApiUserNotFoundException(final String username) {
    super(String.format("github user with username %s not found", username));
  }
}

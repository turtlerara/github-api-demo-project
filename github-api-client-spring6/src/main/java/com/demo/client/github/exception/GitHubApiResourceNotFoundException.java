package com.demo.client.github.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GitHubApiResourceNotFoundException extends GitHubApiException {

  public GitHubApiResourceNotFoundException(final String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}

package com.demo.client.github.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class GitHubApiException extends RuntimeException {

  private final HttpStatusCode statusCode;

  public GitHubApiException(final HttpStatusCode statusCode, final String message) {
    super(String.format("(%d) %s", statusCode.value(), message));
    this.statusCode = statusCode;
  }
}

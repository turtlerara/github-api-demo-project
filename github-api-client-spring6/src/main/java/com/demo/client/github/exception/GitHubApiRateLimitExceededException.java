package com.demo.client.github.exception;

import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class GitHubApiRateLimitExceededException extends GitHubApiException {

  private static final String ERROR_MSG = "github api request rate limit exceeded";

  private final Instant rateLimitResetDate;
  private final Duration retryAfterDuration;

  public GitHubApiRateLimitExceededException(
      final HttpStatusCode statusCode,
      final Instant rateLimitResetDate,
      final Duration retryAfterDuration) {

    super(statusCode, ERROR_MSG);
    this.rateLimitResetDate = rateLimitResetDate;
    this.retryAfterDuration = retryAfterDuration;
  }
}

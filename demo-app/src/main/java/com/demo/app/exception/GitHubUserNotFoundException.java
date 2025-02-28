package com.demo.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GitHubUserNotFoundException extends RuntimeException {

  public GitHubUserNotFoundException(final String username) {
    super(String.format("github user %s not found", username));
  }
}

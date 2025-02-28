package com.demo.client.github.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GitHubUserApi implements GitHubApi {
  USERS_BY_NAME("https://api.github.com/users/{username}"),
  USER_REPOSITORIES("https://api.github.com/users/{username}/repos");

  private final String url;
}

package com.demo.client.github.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GitHubApiVersion {
  LATEST(null),
  V_2022_11_28("2022-11-28");

  private final String headerValue;
}

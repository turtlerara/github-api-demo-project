package com.demo.app.domain;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public record GitHubUserData(
    String username,
    String displayName,
    String avatarUrl,
    String geoLocation,
    String emailAddress,
    String profileUrl,
    Instant createdAtDate,
    @Singular List<UserRepository> repositories) {

  @Builder
  public record UserRepository(String repositoryName, String repositoryUrl) {}
}

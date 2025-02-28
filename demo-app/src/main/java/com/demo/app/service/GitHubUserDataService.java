package com.demo.app.service;

import com.demo.app.domain.GitHubUserData;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface GitHubUserDataService {

  /**
   * Finds a GitHub user's data from the GitHub platform.
   *
   * @param username github username
   * @return an {@link Optional} containing the github user's data if the user exists
   */
  Optional<GitHubUserData> findUserData(@Nullable String username);
}

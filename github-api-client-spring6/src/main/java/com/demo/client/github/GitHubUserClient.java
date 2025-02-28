package com.demo.client.github;

import com.demo.client.github.dto.GitHubUserDto;
import com.demo.client.github.dto.GitHubUserRepositoryDto;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GitHubUserClient {

  /**
   * Finds a user in GitHub.
   *
   * @param username name of the user to find
   * @return an {@link Optional} containing the user, if it exists
   */
  Optional<GitHubUserDto> findUser(String username);

  /**
   * Gathers a list of repositories owned by a GitHub user.
   *
   * @param username name of the user
   * @return list of the user's repositories, if any
   */
  List<GitHubUserRepositoryDto> getUserRepositories(String username);
}

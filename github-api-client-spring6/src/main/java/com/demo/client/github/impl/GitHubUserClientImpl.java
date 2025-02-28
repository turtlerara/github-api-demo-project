package com.demo.client.github.impl;

import static com.demo.client.github.api.GitHubUserApi.USERS_BY_NAME;
import static com.demo.client.github.api.GitHubUserApi.USER_REPOSITORIES;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.demo.client.github.GitHubUserClient;
import com.demo.client.github.dto.GitHubUserDto;
import com.demo.client.github.dto.GitHubUserRepositoryDto;
import com.demo.client.github.exception.GitHubApiResourceNotFoundException;
import com.demo.client.github.exception.GitHubApiUserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

@NullMarked
class GitHubUserClientImpl implements GitHubUserClient {

  private static final ParameterizedTypeReference<List<GitHubUserRepositoryDto>>
      USER_REPO_LIST_TYPE_REF = new ParameterizedTypeReference<>() {};

  private final RestClient restClient;

  GitHubUserClientImpl(RestClient restClient) {
    this.restClient = restClient;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<GitHubUserDto> findUser(final String username) {
    try {
      return ofNullable(
          restClient
              .get()
              .uri(USERS_BY_NAME.getUrl(), username)
              .accept(APPLICATION_JSON)
              .retrieve()
              .body(GitHubUserDto.class));
    } catch (GitHubApiResourceNotFoundException e) {
      return Optional.empty();
    }
  }

  /** {@inheritDoc} */
  @Override
  public List<GitHubUserRepositoryDto> getUserRepositories(final String username) {
    try {
      return ofNullable(
              restClient
                  .get()
                  .uri(USER_REPOSITORIES.getUrl(), username)
                  .accept(APPLICATION_JSON)
                  .retrieve()
                  .body(USER_REPO_LIST_TYPE_REF))
          .orElseGet(List::of);
    } catch (GitHubApiResourceNotFoundException e) {
      throw new GitHubApiUserNotFoundException(username);
    }
  }
}

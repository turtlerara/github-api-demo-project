package com.demo.app.service.impl;

import static com.demo.app.config.CachingConfig.GITHUB_USER_DATA_CACHE;
import static java.util.Optional.ofNullable;

import com.demo.app.domain.GitHubUserData;
import com.demo.app.exception.GitHubServiceException;
import com.demo.app.mapper.GitHubUserDataMapper;
import com.demo.app.service.GitHubUserDataService;
import com.demo.client.github.GitHubUserClient;
import com.demo.client.github.dto.GitHubUserDto;
import com.demo.client.github.dto.GitHubUserRepositoryDto;
import com.demo.client.github.exception.GitHubApiRateLimitExceededException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@NullMarked
public class GitHubUserDataServiceImpl implements GitHubUserDataService {

  private final GitHubUserClient gitHubUserClient;
  private final GitHubUserDataMapper gitHubUserDataMapper;
  private final CacheManager cacheManager;
  @Nullable private volatile Instant rateLimitResetDate;

  public GitHubUserDataServiceImpl(
      GitHubUserClient gitHubUserClient,
      GitHubUserDataMapper gitHubUserDataMapper,
      CacheManager cacheManager) {

    this.gitHubUserClient = gitHubUserClient;
    this.gitHubUserDataMapper = gitHubUserDataMapper;
    this.cacheManager = cacheManager;
  }

  private boolean waitingForRateLimitReset() {
    return ofNullable(rateLimitResetDate)
        .filter(resetDate -> resetDate.isAfter(Instant.now()))
        .isPresent();
  }

  /** {@inheritDoc} */
  @Override
  @CachePut(value = GITHUB_USER_DATA_CACHE, key = "#username")
  public Optional<GitHubUserData> findUserData(@Nullable final String username) {
    return ofNullable(username)
        .filter(StringUtils::isNotBlank)
        .map(
            name -> {
              if (waitingForRateLimitReset()) {
                return handleRateLimitExceeded(username);
              } else {
                try {
                  return getUser(username)
                      .map(
                          user ->
                              gitHubUserDataMapper.toGitHubUserData(
                                  user, getUserRepositories(username)))
                      .orElse(null);
                } catch (GitHubApiRateLimitExceededException e) {
                  return handleRateLimitExceededException(username, e);
                }
              }
            });
  }

  private Optional<GitHubUserDto> getUser(final String username) {
    return gitHubUserClient.findUser(username);
  }

  private List<GitHubUserRepositoryDto> getUserRepositories(final String username) {
    return gitHubUserClient.getUserRepositories(username);
  }

  private GitHubUserData handleRateLimitExceeded(final String username) {
    log.warn(
        "github request rate limit has been exceeded! "
            + "will attempt to get data for github username {} from cache "
            + "(rateLimitResetDate: {})",
        username,
        rateLimitResetDate);

    return getUserDataFromCache(username);
  }

  private GitHubUserData handleRateLimitExceededException(
      final String username, final GitHubApiRateLimitExceededException thrownException) {

    log.warn(
        "github request rate limit has been exceeded! "
            + "will attempt to get data for github username {} from cache "
            + "(rateLimitResetDate: {}, retryAfterDuration: {})",
        username,
        thrownException.getRateLimitResetDate(),
        thrownException.getRetryAfterDuration(),
        thrownException);

    this.rateLimitResetDate = thrownException.getRateLimitResetDate();
    return getUserDataFromCache(username);
  }

  private GitHubUserData getUserDataFromCache(final String username) {
    final Optional<GitHubUserData> cachedUser =
        ofNullable(cacheManager.getCache(GITHUB_USER_DATA_CACHE))
            .map(cache -> cache.get(username))
            .map(ValueWrapper::get)
            .filter(o -> o instanceof GitHubUserData)
            .map(o -> (GitHubUserData) o);

    if (cachedUser.isPresent()) {
      log.info("found github username {} in the cache", username);
      return cachedUser.get();
    } else {
      log.warn("could not find github username {} in the cache", username);
      throw new GitHubServiceException("could not retrieve data for github user " + username);
    }
  }
}

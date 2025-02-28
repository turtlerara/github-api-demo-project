package com.demo.client.github.spring;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.USER_AGENT;

import com.demo.client.github.config.GitHubApiClientConfig;
import com.demo.client.github.config.GitHubApiVersion;
import com.demo.client.github.exception.GitHubApiException;
import com.demo.client.github.exception.GitHubApiRateLimitExceededException;
import com.demo.client.github.exception.GitHubApiResourceNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

@Slf4j
@NullMarked
public final class RestClientFactory {

  private static final String VERSION_HEADER = "X-GitHub-Api-Version";
  private static final String RATE_LIMIT_REMAIN_HEADER = "x-ratelimit-remaining";
  private static final String RATE_LIMIT_RESET_HEADER = "x-ratelimit-reset";
  private static final List<HttpStatus> RATE_LIMIT_EXCEEDED_STATUSES =
      List.of(HttpStatus.FORBIDDEN, HttpStatus.TOO_MANY_REQUESTS);

  private final RestClient.Builder restClientBuilder;

  /**
   * Constructs a {@link RestClientFactory} instance using the provided client configuration.
   *
   * @param clientConfig client configuration
   */
  public RestClientFactory(final GitHubApiClientConfig clientConfig) {
    this.restClientBuilder = initRestClientBuilder(clientConfig);
  }

  /**
   * Builds a Spring {@link RestClient} using the provided {@link GitHubApiClientConfig} client
   * configuration.
   *
   * @return built spring rest client
   */
  public RestClient restClient() {
    return restClientBuilder.build();
  }

  private RestClient.Builder initRestClientBuilder(final GitHubApiClientConfig clientConfig) {
    final RestClient.Builder restClientBuilder =
        RestClient.builder()
            .defaultHeader(USER_AGENT, clientConfig.getUserAgent())
            .defaultStatusHandler(
                HttpStatusCode::isError, (request, response) -> handleErrorResponse(response));

    final GitHubApiVersion apiVersion = clientConfig.getApiVersion();
    if (GitHubApiVersion.LATEST != apiVersion) {
      restClientBuilder.defaultHeader(VERSION_HEADER, apiVersion.getHeaderValue());
    }

    return restClientBuilder;
  }

  private static void handleErrorResponse(final ClientHttpResponse response) throws IOException {
    final HttpStatusCode statusCode = response.getStatusCode();
    final String statusText = response.getStatusText();

    if (log.isDebugEnabled()) {
      log.debug(
          "github api responded with error status code {} >> {}",
          statusCode,
          new String(response.getBody().readAllBytes()));
    }

    if (HttpStatus.NOT_FOUND.equals(statusCode)) {
      throw new GitHubApiResourceNotFoundException(statusText);
    }
    if (isRateLimitExceeded(response)) {
      throw new GitHubApiRateLimitExceededException(
          statusCode,
          getRateLimitResetDateValue(response).orElse(null),
          getRetryAfterDurationValue(response).orElse(null));
    }
    throw new GitHubApiException(statusCode, statusText);
  }

  private static List<String> getHeaderValues(
      final String header, @Nullable final ClientHttpResponse response) {

    return ofNullable(response)
        .map(ClientHttpResponse::getHeaders)
        .map(headers -> headers.get(header))
        .orElseGet(List::of);
  }

  private static Optional<String> getHeaderValue(
      final String header, @Nullable final ClientHttpResponse response) {

    return getHeaderValues(header, response).stream().findFirst();
  }

  private static Optional<Integer> getRateLimitRemainingValue(
      @Nullable final ClientHttpResponse response) {

    return getHeaderValue(RATE_LIMIT_REMAIN_HEADER, response)
        .filter(StringUtils::isNumeric)
        .map(Integer::parseInt);
  }

  private static Optional<Instant> getRateLimitResetDateValue(
      @Nullable final ClientHttpResponse response) {

    return getHeaderValue(RATE_LIMIT_RESET_HEADER, response)
        .filter(StringUtils::isNumeric)
        .map(Long::parseLong)
        .map(Instant::ofEpochSecond);
  }

  private static Optional<Duration> getRetryAfterDurationValue(
      @Nullable final ClientHttpResponse response) {

    return getHeaderValue(HttpHeaders.RETRY_AFTER, response)
        .filter(StringUtils::isNumeric)
        .map(Long::parseLong)
        .map(Duration::ofSeconds);
  }

  private static boolean isRateLimitExceeded(@Nullable final ClientHttpResponse response)
      throws IOException {

    boolean isRateLimitExceeded = false;

    if (ofNullable(response).isPresent()) {
      final HttpStatusCode statusCode = response.getStatusCode();
      if (RATE_LIMIT_EXCEEDED_STATUSES.contains((HttpStatus) statusCode)) {
        isRateLimitExceeded = isRateLimitRemainingZero(response);
      }
    }
    return isRateLimitExceeded;
  }

  private static boolean isRateLimitRemainingZero(@Nullable final ClientHttpResponse response) {
    return getRateLimitRemainingValue(response).filter(remaining -> remaining == 0).isPresent();
  }
}

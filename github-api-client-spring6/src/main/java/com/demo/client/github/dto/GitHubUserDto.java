package com.demo.client.github.dto;

import static java.util.Optional.ofNullable;

import com.demo.client.github.util.GitHubDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUserDto {

  /** User Login */
  String login;

  /** User's Name */
  String name;

  /** User's Profile URL */
  @JsonProperty("url")
  String profileUrl;

  /** User's Avatar URL */
  @JsonProperty("avatar_url")
  String avatarUrl;

  /** User's Location */
  String location;

  /** User's Email Address */
  @JsonProperty("email")
  String emailAddress;

  /** Date and time when the user was created. (ISO-8601) */
  @JsonProperty("created_at")
  String createdAtTimestamp;

  /**
   * Gets the instant in time when the user was created.
   *
   * @return date and time the user was created as an {@link Instant}
   */
  @JsonIgnore
  public Instant getCreatedAtDate() {
    return ofNullable(createdAtTimestamp).map(GitHubDateUtil::iso8601ToInstant).orElse(null);
  }
}

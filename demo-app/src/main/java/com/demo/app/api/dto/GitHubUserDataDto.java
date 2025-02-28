package com.demo.app.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class GitHubUserDataDto {

  private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
  private static final String DATE_TZ = "UTC";

  /** GitHub Username */
  @JsonProperty("user_name")
  @Schema(description = "GitHub Username", example = "octocat")
  String username;

  /** GitHub Display Name */
  @JsonProperty("display_name")
  @Schema(description = "GitHub Display Name", example = "The Octocat")
  String displayName;

  /** GitHub User Avatar URL */
  @JsonProperty("avatar")
  @Schema(
      description = "User's Avatar URL",
      example = "https://avatars3.githubusercontent.com/u/583231")
  String avatarUrl;

  /** GitHub User Geo Location */
  @JsonProperty("geo_location")
  @Schema(description = "User's Location", example = "San Francisco")
  String geoLocation;

  /** GitHub User Email Address */
  @JsonProperty("email")
  @Schema(description = "User's Email Address", example = "user@github.com")
  String emailAddress;

  /** GitHub User Profile URL */
  @JsonProperty("url")
  @Schema(description = "User's Profile URL", example = "https://github.com/octocat")
  String profileUrl;

  /** Date and time the GitHub user was created. */
  @JsonProperty("created_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = DATE_TZ)
  @Schema(description = "GitHub User Create Date", type = "date-time")
  Instant createdAtDate;

  @JsonProperty("repos")
  @Schema(description = "User's GitHub Repositories")
  @Singular
  List<GitHubRepositoryDataDto> repositories;
}

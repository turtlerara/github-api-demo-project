package com.demo.app.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class GitHubRepositoryDataDto {

  /** GitHub Repository Name */
  @JsonProperty("name")
  @Schema(description = "GitHub Repository Name", example = "octocat")
  String repositoryName;

  /** GitHub Repository URL */
  @JsonProperty("url")
  @Schema(
      description = "GitHub Repository URL",
      example = "https://github.com/octocat/boysenberry-repo-1")
  String repositoryUrl;
}

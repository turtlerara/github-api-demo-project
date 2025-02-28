package com.demo.client.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUserRepositoryDto {

  /** Repository Name */
  @JsonProperty("name")
  String repositoryName;

  /** Repository URL */
  @JsonProperty("url")
  String repositoryUrl;
}

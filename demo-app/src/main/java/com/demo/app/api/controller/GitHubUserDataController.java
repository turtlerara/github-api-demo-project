package com.demo.app.api.controller;

import static com.demo.app.util.TextUtil.toSanitizedText;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.demo.app.api.dto.GitHubUserDataDto;
import com.demo.app.exception.GitHubUserNotFoundException;
import com.demo.app.mapper.GitHubUserDataMapper;
import com.demo.app.service.GitHubUserDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "GitHub User Data", description = "APIs for retrieving data for GitHub users.")
public class GitHubUserDataController {

  private static final int MAX_GH_USERNAME_LENGTH = 39;
  private static final String GH_USERNAME_REGEX = "^[a-zA-Z0-9]+(?:-[a-zA-Z0-9]+)*$";

  private final GitHubUserDataMapper gitHubUserDataMapper;
  private final GitHubUserDataService gitHubUserDataService;

  public GitHubUserDataController(
      GitHubUserDataMapper gitHubUserDataMapper, GitHubUserDataService gitHubUserDataService) {

    this.gitHubUserDataMapper = gitHubUserDataMapper;
    this.gitHubUserDataService = gitHubUserDataService;
  }

  @Operation(summary = "Get data for a GitHub user")
  @GetMapping(value = "/api/v1/github-users/{username}", produces = APPLICATION_JSON_VALUE)
  public GitHubUserDataDto getUserData(
      @NotBlank(message = "supplied username cannot be blank")
          @Size(
              max = MAX_GH_USERNAME_LENGTH,
              message = "github usernames cannot exceed " + MAX_GH_USERNAME_LENGTH + " characters")
          @Pattern(
              regexp = GH_USERNAME_REGEX,
              message = "supplied username does not match the expected github username pattern")
          @PathVariable
          final String username) {

    final String sanitizedUsername = toSanitizedText(username);
    return gitHubUserDataService
        .findUserData(sanitizedUsername)
        .map(gitHubUserDataMapper::toGitHubUserDataDto)
        .orElseThrow(() -> new GitHubUserNotFoundException(username));
  }
}

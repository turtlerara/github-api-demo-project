package com.demo.app.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.app.api.dto.GitHubUserDataDto;
import com.demo.app.domain.GitHubUserData;
import com.demo.app.domain.GitHubUserData.UserRepository;
import com.demo.app.mapper.GitHubUserDataMapper;
import com.demo.app.mapper.GitHubUserDataMapperImpl;
import com.demo.app.service.GitHubUserDataService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(controllers = {GitHubUserDataController.class})
@AutoConfigureMockMvc
public class GitHubUserDataControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private GitHubUserDataService gitHubUserDataService;
  @MockitoBean private GitHubUserDataMapper gitHubUserDataMapper;

  private final String username = "octocat";

  @Test
  void get_github_user_data() throws Exception {
    final GitHubUserDataMapper mapper = new GitHubUserDataMapperImpl();
    final UserRepository repository = UserRepository.builder().repositoryName("repository").build();
    final GitHubUserData userData =
        GitHubUserData.builder().username(username).repository(repository).build();
    final GitHubUserDataDto userDataDto = mapper.toGitHubUserDataDto(userData);

    when(gitHubUserDataService.findUserData(username)).thenReturn(Optional.of(userData));
    when(gitHubUserDataMapper.toGitHubUserDataDto(userData)).thenReturn(userDataDto);

    mockMvc
        .perform(get("/api/v1/github-users/{username}", username))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user_name").value(username))
        .andExpect(jsonPath("$.repos", hasSize(1)))
        .andExpect(jsonPath("$.repos[0].name").value(repository.repositoryName()));
  }

  @Test
  void github_user_not_found() throws Exception {
    when(gitHubUserDataService.findUserData(username)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/v1/github-users/{username}", username))
        .andExpect(status().isNotFound());
  }

  @Test
  void invalid_github_username() throws Exception {
    mockMvc
        .perform(get("/api/v1/github-users/{username}", "-" + username))
        .andExpect(status().isBadRequest());
  }

  @Test
  void github_username_too_long() throws Exception {
    final String username = "abcdefghijklmnopqrstuvwxyz12345678900000";
    mockMvc
        .perform(get("/api/v1/github-users/{username}", username))
        .andExpect(status().isBadRequest());
  }
}

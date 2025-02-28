package com.demo.app.mapper;

import com.demo.app.api.dto.GitHubUserDataDto;
import com.demo.app.config.SpringMapStructConfig;
import com.demo.app.domain.GitHubUserData;
import com.demo.client.github.dto.GitHubUserDto;
import com.demo.client.github.dto.GitHubUserRepositoryDto;
import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface GitHubUserDataMapper {

  @Mapping(target = "username", source = "user.login")
  @Mapping(target = "displayName", source = "user.name")
  @Mapping(target = "geoLocation", source = "user.location")
  @Mapping(target = "repository", ignore = true)
  GitHubUserData toGitHubUserData(
      GitHubUserDto user, Collection<GitHubUserRepositoryDto> repositories);

  GitHubUserData.UserRepository toGitHubUserRepository(GitHubUserRepositoryDto repository);

  @Mapping(target = "repository", ignore = true)
  GitHubUserDataDto toGitHubUserDataDto(GitHubUserData userData);
}

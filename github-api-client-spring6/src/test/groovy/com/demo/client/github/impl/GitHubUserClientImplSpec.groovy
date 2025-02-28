package com.demo.client.github.impl

import com.demo.client.github.api.GitHubUserApi
import com.demo.client.github.config.GitHubApiClientConfig
import com.demo.client.github.dto.GitHubUserDto
import com.demo.client.github.dto.GitHubUserRepositoryDto
import com.demo.client.github.exception.GitHubApiRateLimitExceededException
import com.demo.client.github.exception.GitHubApiResourceNotFoundException
import com.demo.client.github.exception.GitHubApiUserNotFoundException
import com.demo.client.github.spring.RestClientFactory
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import spock.lang.Shared
import spock.lang.Specification

@SuppressWarnings('GroovyAccessibility')
class GitHubUserClientImplSpec extends Specification {

  GitHubUserClientImpl client

  //def clientConfig = GitHubApiClientConfig.builder().build()
  //def realRestClient = new RestClientFactory(clientConfig).restClient()

  def mockRestClient = Mock(RestClient)
  def mockRequestHeadersUriSpec = Mock(RestClient.RequestHeadersUriSpec)
  def mockRequestBodySpec = Mock(RestClient.RequestBodySpec)
  def mockResponseSpec = Mock(RestClient.ResponseSpec)

  def setup() {
    client = new GitHubUserClientImpl(mockRestClient)
  }

  @Shared
  def username = 'octocat'

  def 'get a github user'() {
    given:
    def userDto = GitHubUserDto.builder().build()

    when:
    def result = client.findUser(username)

    then:
    result.get() == userDto
    1 * mockRestClient.get() >> mockRequestHeadersUriSpec
    1 * mockRequestHeadersUriSpec.uri({
      String url -> url == GitHubUserApi.USERS_BY_NAME.url
    }, username) >> mockRequestBodySpec
    1 * mockRequestBodySpec.accept(MediaType.APPLICATION_JSON) >> mockRequestBodySpec
    1 * mockRequestBodySpec.retrieve() >> mockResponseSpec
    1 * mockResponseSpec.body(GitHubUserDto.class) >> userDto
    0 * _
  }

  def 'get a github user that doesn\'t exist'() {
    when:
    def result = client.findUser(username)

    then:
    result.isEmpty()
    1 * mockRestClient.get() >> mockRequestHeadersUriSpec
    1 * mockRequestHeadersUriSpec.uri({
      String url -> url == GitHubUserApi.USERS_BY_NAME.url
    }, username) >> mockRequestBodySpec
    1 * mockRequestBodySpec.accept(MediaType.APPLICATION_JSON) >> mockRequestBodySpec
    1 * mockRequestBodySpec.retrieve() >> mockResponseSpec
    1 * mockResponseSpec.body(GitHubUserDto.class) >> {
      throw new GitHubApiResourceNotFoundException('user not found')
    }
    0 * _
  }

  def 'get a github user but rate limit exceeded'() {
    when:
    client.findUser(username)

    then:
    thrown GitHubApiRateLimitExceededException
    1 * mockRestClient.get() >> mockRequestHeadersUriSpec
    1 * mockRequestHeadersUriSpec.uri({
      String url -> url == GitHubUserApi.USERS_BY_NAME.url
    }, username) >> mockRequestBodySpec
    1 * mockRequestBodySpec.accept(MediaType.APPLICATION_JSON) >> mockRequestBodySpec
    1 * mockRequestBodySpec.retrieve() >> mockResponseSpec
    1 * mockResponseSpec.body(GitHubUserDto.class) >> {
      throw Mock(GitHubApiRateLimitExceededException)
    }
    0 * _
  }

  def 'get a github user\'s repositories'() {
    given:
    def repoDto = GitHubUserRepositoryDto.builder().build()

    when:
    def results = client.getUserRepositories(username)

    then:
    results.size() == 1
    results.first == repoDto
    1 * mockRestClient.get() >> mockRequestHeadersUriSpec
    1 * mockRequestHeadersUriSpec.uri({
      String url -> url == GitHubUserApi.USER_REPOSITORIES.url
    }, username) >> mockRequestBodySpec
    1 * mockRequestBodySpec.accept(MediaType.APPLICATION_JSON) >> mockRequestBodySpec
    1 * mockRequestBodySpec.retrieve() >> mockResponseSpec
    1 * mockResponseSpec.body(GitHubUserClientImpl.USER_REPO_LIST_TYPE_REF) >> [repoDto]
    0 * _
  }

  def 'get a github user\'s repositories but the user doesn\'t exist'() {
    when:
    client.getUserRepositories(username)

    then:
    thrown GitHubApiUserNotFoundException
    1 * mockRestClient.get() >> mockRequestHeadersUriSpec
    1 * mockRequestHeadersUriSpec.uri({
      String url -> url == GitHubUserApi.USER_REPOSITORIES.url
    }, username) >> mockRequestBodySpec
    1 * mockRequestBodySpec.accept(MediaType.APPLICATION_JSON) >> mockRequestBodySpec
    1 * mockRequestBodySpec.retrieve() >> mockResponseSpec
    1 * mockResponseSpec.body(GitHubUserClientImpl.USER_REPO_LIST_TYPE_REF) >> {
      throw new GitHubApiResourceNotFoundException('user not found')
    }
    0 * _
  }

  def 'get a github user\'s repositories but rate limit exceeded'() {
    when:
    client.getUserRepositories(username)

    then:
    thrown GitHubApiRateLimitExceededException
    1 * mockRestClient.get() >> mockRequestHeadersUriSpec
    1 * mockRequestHeadersUriSpec.uri({
      String url -> url == GitHubUserApi.USER_REPOSITORIES.url
    }, username) >> mockRequestBodySpec
    1 * mockRequestBodySpec.accept(MediaType.APPLICATION_JSON) >> mockRequestBodySpec
    1 * mockRequestBodySpec.retrieve() >> mockResponseSpec
    1 * mockResponseSpec.body(GitHubUserClientImpl.USER_REPO_LIST_TYPE_REF) >> {
      throw Mock(GitHubApiRateLimitExceededException)
    }
    0 * _
  }
}

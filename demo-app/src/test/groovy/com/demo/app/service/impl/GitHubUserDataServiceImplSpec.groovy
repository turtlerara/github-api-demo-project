package com.demo.app.service.impl

import com.demo.app.domain.GitHubUserData
import com.demo.app.exception.GitHubServiceException
import com.demo.app.mapper.GitHubUserDataMapperImpl
import com.demo.client.github.GitHubUserClient
import com.demo.client.github.dto.GitHubUserDto
import com.demo.client.github.dto.GitHubUserRepositoryDto
import com.demo.client.github.exception.GitHubApiRateLimitExceededException
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant

import static com.demo.app.config.CachingConfig.GITHUB_USER_DATA_CACHE

@SuppressWarnings('GroovyAccessibility')
class GitHubUserDataServiceImplSpec extends Specification {

  GitHubUserDataServiceImpl service

  def mockUserClient = Mock(GitHubUserClient)
  def mockCacheManager = Mock(CacheManager)

  def setup() {
    service = new GitHubUserDataServiceImpl(
        mockUserClient,
        new GitHubUserDataMapperImpl(),
        mockCacheManager)
  }

  @Shared
  def username = 'octocat'

  def 'get user data'() {
    given:
    def userDto = GitHubUserDto.builder()
        .login(username)
        .build()
    def repoDto = GitHubUserRepositoryDto.builder()
        .repositoryName('repo')
        .build()

    when:
    def result = service.findUserData(username)

    then:
    !result.isEmpty()
    result.get().username() == username
    result.get().repositories().size() == 1
    result.get().repositories().first.repositoryName() == repoDto.repositoryName
    1 * mockUserClient.findUser(username) >> Optional.of(userDto)
    1 * mockUserClient.getUserRepositories(username) >> [repoDto]
    0 * _
  }

  def 'get user data but user not found'() {
    when:
    def result = service.findUserData(username)

    then:
    result.isEmpty()
    1 * mockUserClient.findUser(username) >> Optional.empty()
    0 * _
  }

  def 'get user data but rate limit exceeded'() {
    given:
    def mockCache = Mock(Cache)
    def mockCacheValueWrapper = Mock(Cache.ValueWrapper)
    def rateLimitResetDate = Instant.now().minusSeconds(60)
    def userData = GitHubUserData.builder().build()

    when:
    def result = service.findUserData(username)

    then:
    !result.isEmpty()
    result.get() == userData
    service.rateLimitResetDate == rateLimitResetDate
    1 * mockUserClient.findUser(username) >> {
      throw new GitHubApiRateLimitExceededException(HttpStatus.FORBIDDEN, rateLimitResetDate, null)
    }
    1 * mockCacheManager.getCache(GITHUB_USER_DATA_CACHE) >> mockCache
    1 * mockCache.get(username) >> mockCacheValueWrapper
    1 * mockCacheValueWrapper.get() >> userData
    0 * _
  }

  def 'get user data and repos but rate limit exceeded'() {
    given:
    def mockCache = Mock(Cache)
    def mockCacheValueWrapper = Mock(Cache.ValueWrapper)
    def rateLimitResetDate = Instant.now().minusSeconds(60)
    def userDto = GitHubUserDto.builder()
        .login(username)
        .build()
    def userData = GitHubUserData.builder().build()

    when:
    def result = service.findUserData(username)

    then:
    !result.isEmpty()
    result.get() == userData
    service.rateLimitResetDate == rateLimitResetDate
    1 * mockUserClient.findUser(username) >> Optional.of(userDto)
    1 * mockUserClient.getUserRepositories(username) >> {
      throw new GitHubApiRateLimitExceededException(HttpStatus.FORBIDDEN, rateLimitResetDate, null)
    }
    1 * mockCacheManager.getCache(GITHUB_USER_DATA_CACHE) >> mockCache
    1 * mockCache.get(username) >> mockCacheValueWrapper
    1 * mockCacheValueWrapper.get() >> userData
    0 * _
  }

  def 'get user data while waiting for rate limit to reset'() {
    given:
    def mockCache = Mock(Cache)
    def mockCacheValueWrapper = Mock(Cache.ValueWrapper)
    def rateLimitResetDate = Instant.now().plusSeconds(60)
    def userData = GitHubUserData.builder().build()

    when:
    service.rateLimitResetDate = rateLimitResetDate
    def result = service.findUserData(username)

    then:
    !result.isEmpty()
    result.get() == userData
    1 * mockCacheManager.getCache(GITHUB_USER_DATA_CACHE) >> mockCache
    1 * mockCache.get(username) >> mockCacheValueWrapper
    1 * mockCacheValueWrapper.get() >> userData
    0 * _
  }

  def 'get user data while waiting for rate limit to reset - user not in cache'() {
    given:
    def mockCache = Mock(Cache)
    def mockCacheValueWrapper = Mock(Cache.ValueWrapper)
    def rateLimitResetDate = Instant.now().plusSeconds(60)

    when:
    service.rateLimitResetDate = rateLimitResetDate
    service.findUserData(username)

    then:
    thrown GitHubServiceException
    1 * mockCacheManager.getCache(GITHUB_USER_DATA_CACHE) >> mockCache
    1 * mockCache.get(username) >> mockCacheValueWrapper
    1 * mockCacheValueWrapper.get() >> null
    0 * _
  }
}

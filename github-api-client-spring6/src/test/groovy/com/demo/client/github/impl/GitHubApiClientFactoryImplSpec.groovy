package com.demo.client.github.impl

import com.demo.client.github.GitHubUserClient
import com.demo.client.github.config.GitHubApiClientConfig
import spock.lang.Specification

class GitHubApiClientFactoryImplSpec extends Specification {

  GitHubApiClientFactoryImpl clientFactory

  def clientConfig = GitHubApiClientConfig.builder().build()

  def setup() {
    clientFactory = new GitHubApiClientFactoryImpl(clientConfig)
  }

  def 'build github user client'() {
    when:
    def userClient = clientFactory.userClient()

    then:
    userClient != null
    userClient instanceof GitHubUserClient
  }
}

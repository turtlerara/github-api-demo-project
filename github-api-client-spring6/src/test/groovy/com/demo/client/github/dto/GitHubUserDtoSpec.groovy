package com.demo.client.github.dto

import spock.lang.Specification

import java.time.Instant

class GitHubUserDtoSpec extends Specification {

  def 'get created at date'() {
    expect:
    GitHubUserDto.builder().createdAtTimestamp(timestamp).build().createdAtDate == expectedResult

    where:
    timestamp              || expectedResult
    '2025-01-15T12:30:00Z' || Instant.parse(timestamp)
    '2025-01-15 12:30:00Z' || null
    ' '                    || null
    ''                     || null
    null                   || null
  }
}

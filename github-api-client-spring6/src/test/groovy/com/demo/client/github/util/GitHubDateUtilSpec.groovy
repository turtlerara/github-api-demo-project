package com.demo.client.github.util

import spock.lang.Specification

import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

class GitHubDateUtilSpec extends Specification {

  def now = Instant.now()
  def iso8601Timestamp = '2025-01-15T12:30:00Z'
  def iso8601TimestampWithMillis = '2025-01-15T12:30:00.123Z'

  def 'convert iso-8601 timestamp to instant'() {
    when:
    def result = GitHubDateUtil.iso8601ToInstant(iso8601Timestamp)

    then:
    result != null
    result.isBefore(now)
    LocalDateTime.ofInstant(result, ZoneId.systemDefault()).month == Month.JANUARY
  }

  def 'convert iso-8601 timestamp with milliseconds to instant'() {
    when:
    def result = GitHubDateUtil.iso8601ToInstant(iso8601TimestampWithMillis)

    then:
    result != null
    result.isBefore(now)
    LocalDateTime.ofInstant(result, ZoneId.systemDefault()).month == Month.JANUARY
  }
}

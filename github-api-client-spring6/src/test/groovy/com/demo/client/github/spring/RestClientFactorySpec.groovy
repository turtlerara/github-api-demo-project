package com.demo.client.github.spring

import com.demo.client.github.config.GitHubApiClientConfig
import com.demo.client.github.exception.GitHubApiException
import com.demo.client.github.exception.GitHubApiRateLimitExceededException
import com.demo.client.github.exception.GitHubApiResourceNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestClient
import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@SuppressWarnings('GroovyAccessibility')
class RestClientFactorySpec extends Specification {

  RestClientFactory restClientFactory

  def setup() {
    restClientFactory = new RestClientFactory(GitHubApiClientConfig.builder().build())
  }

  def 'build rest client'() {
    when:
    def restClient = restClientFactory.restClient()

    then:
    restClient != null
    restClient instanceof RestClient
  }

  def 'handle 404 not found error response'() {
    given:
    def mockResponse = Mock(ClientHttpResponse)
    def errorStatusCode = HttpStatus.NOT_FOUND
    def errorStatusText = 'not found'

    when:
    restClientFactory.handleErrorResponse(mockResponse)

    then:
    def error = thrown GitHubApiResourceNotFoundException
    error.statusCode == errorStatusCode
    error.message == "(${errorStatusCode.value()}) $errorStatusText"
    1 * mockResponse.statusCode >> errorStatusCode
    1 * mockResponse.statusText >> errorStatusText
    0 * _
  }

  def 'handle 400 and 500 error responses'() {
    given:
    def mockResponse = Mock(ClientHttpResponse)
    def errorStatusText = 'whoops'

    when:
    restClientFactory.handleErrorResponse(mockResponse)

    then:
    def error = thrown GitHubApiException
    error.statusCode == errorStatusCode
    error.message == "(${errorStatusCode.value()}) $errorStatusText"
    2 * mockResponse.statusCode >> errorStatusCode
    1 * mockResponse.statusText >> errorStatusText
    0 * _

    where:
    errorStatusCode << [HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR]
  }

  def 'handle 403 forbidden error response that is not a rate limit error'() {
    given:
    def mockResponse = Mock(ClientHttpResponse)
    def errorStatusCode = HttpStatus.FORBIDDEN
    def errorStatusText = 'whoops'
    def errorHeaders = new HttpHeaders()
    errorHeaders.put(RestClientFactory.RATE_LIMIT_REMAIN_HEADER, List.of('5'))

    when:
    restClientFactory.handleErrorResponse(mockResponse)

    then:
    def error = thrown GitHubApiException
    error.statusCode == errorStatusCode
    error.message == "(${errorStatusCode.value()}) $errorStatusText"
    2 * mockResponse.statusCode >> errorStatusCode
    1 * mockResponse.statusText >> errorStatusText
    1 * mockResponse.headers >> errorHeaders
    0 * _
  }

  def 'handle primary rate limit error response'() {
    given:
    def mockResponse = Mock(ClientHttpResponse)
    def errorStatusCode = HttpStatus.FORBIDDEN
    def errorStatusText = 'whoops'
    def now = Instant.now().truncatedTo(ChronoUnit.SECONDS)
    def errorHeaders = new HttpHeaders()
    errorHeaders.put(RestClientFactory.RATE_LIMIT_REMAIN_HEADER, List.of('0'))
    errorHeaders.put(RestClientFactory.RATE_LIMIT_RESET_HEADER, List.of('' + now.epochSecond))

    when:
    restClientFactory.handleErrorResponse(mockResponse)

    then:
    def error = thrown GitHubApiRateLimitExceededException
    error.statusCode == errorStatusCode
    error.rateLimitResetDate == now
    2 * mockResponse.statusCode >> errorStatusCode
    1 * mockResponse.statusText >> errorStatusText
    3 * mockResponse.headers >> errorHeaders
    0 * _
  }

  def 'handle secondary rate limit error response'() {
    given:
    def mockResponse = Mock(ClientHttpResponse)
    def errorStatusCode = HttpStatus.FORBIDDEN
    def errorStatusText = 'whoops'
    def retryAfterSeconds = 10
    def errorHeaders = new HttpHeaders()
    errorHeaders.put(RestClientFactory.RATE_LIMIT_REMAIN_HEADER, List.of('0'))
    errorHeaders.put(HttpHeaders.RETRY_AFTER, List.of('' + retryAfterSeconds))

    when:
    restClientFactory.handleErrorResponse(mockResponse)

    then:
    def error = thrown GitHubApiRateLimitExceededException
    error.statusCode == errorStatusCode
    error.retryAfterDuration == Duration.ofSeconds(retryAfterSeconds)
    2 * mockResponse.statusCode >> errorStatusCode
    1 * mockResponse.statusText >> errorStatusText
    3 * mockResponse.headers >> errorHeaders
    0 * _
  }
}

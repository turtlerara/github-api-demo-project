package com.demo.app.config.properties;

import com.demo.app.annotation.ExcludeFromJacocoGeneratedReport;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/**
 * @param githubUserData properties for the github user data cache
 */
@ConfigurationProperties("caching")
@Validated
@ExcludeFromJacocoGeneratedReport
public record CachingConfigProperties(@DefaultValue CacheProperties githubUserData) {

  private static final String DAILY = "0 0 0 * * *";

  /**
   * @param cacheEvictCron cron schedule for evicting the cache
   */
  @ExcludeFromJacocoGeneratedReport
  public record CacheProperties(@DefaultValue(DAILY) @NotBlank String cacheEvictCron) {}
}

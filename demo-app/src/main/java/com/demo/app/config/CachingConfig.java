package com.demo.app.config;

import static java.util.Optional.ofNullable;

import com.demo.app.annotation.ExcludeFromJacocoGeneratedReport;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@ExcludeFromJacocoGeneratedReport
public class CachingConfig {

  public static final String GITHUB_USER_DATA_CACHE = "gitHubUsers";

  private final CacheManager cacheManager;

  public CachingConfig(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  /** Clears the GitHub user data cache on a fixed schedule. */
  @Scheduled(cron = "${caching.github-user-data.cache-evict-cron}")
  public void clearGitHubUserDataCache() {
    ofNullable(cacheManager.getCache(GITHUB_USER_DATA_CACHE)).ifPresent(Cache::clear);
  }
}

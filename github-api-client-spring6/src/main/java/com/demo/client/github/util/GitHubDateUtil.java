package com.demo.client.github.util;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GitHubDateUtil {

  /**
   * Converts an ISO-8601 timestamp into an instant in time.
   *
   * @param timestamp iso-8601 timestamp
   * @return the timestamp converted into a java {@link Instant}
   */
  public static Instant iso8601ToInstant(final String timestamp) {
    try {
      return Instant.parse(timestamp);
    } catch (Exception e) {
      log.warn("failed to parse timestamp {} to a java instant", timestamp, e);
      return null;
    }
  }
}

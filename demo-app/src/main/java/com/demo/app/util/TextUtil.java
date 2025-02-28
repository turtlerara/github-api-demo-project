package com.demo.app.util;

import static java.util.Optional.ofNullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextUtil {

  public static String toSanitizedText(final String text) {
    return ofNullable(text)
        .filter(StringUtils::isNotBlank)
        .map(StringUtils::normalizeSpace)
        .map(StringEscapeUtils::escapeHtml4)
        .orElse(null);
  }
}

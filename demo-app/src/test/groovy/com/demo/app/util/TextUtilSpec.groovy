package com.demo.app.util

import spock.lang.Specification

class TextUtilSpec extends Specification {

  def 'sanitize text'() {
    expect:
    TextUtil.toSanitizedText(text) == expectedResult

    where:
    text                 || expectedResult
    'hello'              || text
    '   hello world'     || 'hello world'
    'hello <html> world' || 'hello &lt;html&gt; world'
    ' '                  || null
    ''                   || null
    null                 || null
  }
}

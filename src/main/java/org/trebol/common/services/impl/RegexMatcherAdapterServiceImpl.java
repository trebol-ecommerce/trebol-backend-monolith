/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.common.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.config.ValidationProperties;
import org.trebol.common.services.RegexMatcherAdapterService;

import java.util.regex.Pattern;

import static org.trebol.config.Constants.JWT_PREFIX;

@Service
public class RegexMatcherAdapterServiceImpl
  implements RegexMatcherAdapterService {
  private final ValidationProperties validationProperties;
  private Pattern idNumberPattern = null;
  private Pattern jwtTokenPattern = null;

  @Autowired
  public RegexMatcherAdapterServiceImpl(
    ValidationProperties validationProperties
  ) {
    this.validationProperties = validationProperties;
  }

  @Override
  public boolean isAValidIdNumber(String matchAgainst) {
    if (this.idNumberPattern == null) {
      this.idNumberPattern = Pattern.compile(validationProperties.getIdNumberRegexp());
    }
    return this.idNumberPattern.matcher(matchAgainst).matches();
  }

  @Override
  public boolean isAValidAuthorizationHeader(String matchAgainst) {
    if (this.jwtTokenPattern == null) {
      this.jwtTokenPattern = Pattern.compile("^" + JWT_PREFIX + ".+$");
    }
    return this.jwtTokenPattern.matcher(matchAgainst).matches();
  }
}

/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QSell;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.IPredicateJpaService;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Service
public class SalesPredicateJpaServiceImpl
  implements IPredicateJpaService<Sell> {

  private final Logger logger = LoggerFactory.getLogger(SalesPredicateJpaServiceImpl.class);

  @Override
  public QSell getBasePath() {
    return QSell.sell;
  }

  @Override
  public Predicate parseMap(Map<String, String> queryParamsMap) {
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
          case "buyOrder":
            return getBasePath().id.eq(Long.valueOf(stringValue));
          case "date":
            predicate.and(getBasePath().date.eq(Instant.parse(stringValue)));
            break;
          case "statusName":
            predicate.and(getBasePath().status.name.eq(stringValue));
            break;
          case "token":
            predicate.and(getBasePath().transactionToken.eq(stringValue));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      } catch (DateTimeParseException exc) {
        logger.warn("Param '{}' couldn't be parsed as date (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }
}

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

package org.trebol.jpa.services.sortspecs;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QUser;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.GenericSortSpecJpaService;

import java.util.Map;

@Service
public class UsersSortSpecJpaServiceImpl
  extends GenericSortSpecJpaService<User> {

  @Override
  public QUser getBasePath() {
    return QUser.user;
  }

  @Override
  protected Map<String, OrderSpecifier<?>> createOrderSpecMap() {
    return Map.of(
            "name", QUser.user.name.asc(),
            "role", QUser.user.userRole.name.asc()
    );
  }
}

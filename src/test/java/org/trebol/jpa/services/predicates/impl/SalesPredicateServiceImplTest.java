/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
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

package org.trebol.jpa.services.predicates.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SalesPredicateServiceImplTest {
    final SalesPredicateServiceImpl instance = new SalesPredicateServiceImpl();
    ;

    @Test
    void parses_map() {
        Predicate emptyPredicate = new BooleanBuilder();
        List<Predicate> predicates = List.of(emptyPredicate,
            instance.parseMap(Map.of("id", "1")),
            instance.parseMap(Map.of("buyOrder", "1")), // same as id
            instance.parseMap(Map.of("date", Instant.now().toString())),
            instance.parseMap(Map.of("statusName", "test")),
            instance.parseMap(Map.of("token", "test")));
        Set<Predicate> distinctPredicates = new HashSet<>(predicates);
        assertEquals(predicates.size(), distinctPredicates.size() + 1);
    }

    @Test
    void only_accepts_correct_dates() {
        Predicate emptyPredicate = new BooleanBuilder();
        Predicate whereDateIs = instance.parseMap(Map.of("date", Instant.now().toString()));

        assertNotNull(whereDateIs);
        assertNotEquals(whereDateIs, emptyPredicate);

        // invalid date format
        assertEquals(instance.parseMap(Map.of("date", "1")), emptyPredicate);
        assertEquals(instance.parseMap(Map.of("date", "10/10")), emptyPredicate);
        assertEquals(instance.parseMap(Map.of("date", "1984")), emptyPredicate);
        assertEquals(instance.parseMap(Map.of("date", "!?")), emptyPredicate);
        assertEquals(instance.parseMap(Map.of("date", "not a date")), emptyPredicate);
    }
}

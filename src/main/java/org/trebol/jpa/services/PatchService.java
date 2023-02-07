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

package org.trebol.jpa.services;

import org.trebol.common.exceptions.BadInputException;

/**
 * Type-safe interface for passing data from Pojos to Entities in order to
 * prepare these for submission to the persistence layer.
 *
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public interface PatchService<P, E> {

  /**
   * Creates a clone Entity, then updates it with new data from a Pojo,
   * setting differences in properties one-by-one, and returns it.
   * It does not include relationships to other entities.
   *
   * @param changes The Pojo containing data updates.
   * @param target The target entity.
   * @return An updated instance of the @Entity, prepared to be saved to the database.
   * @throws BadInputException If the object with changes has invalid values
   */
  E patchExistingEntity(P changes, E target) throws BadInputException;
}

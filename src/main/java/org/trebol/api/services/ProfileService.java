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

package org.trebol.api.services;

import org.trebol.api.models.PersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.exceptions.UserNotFoundException;

import javax.persistence.EntityNotFoundException;

/**
 * Provides means for users to retrieve and update their stored personal information.
 */
public interface ProfileService {

  /**
   * Fetches personal information from a given username in a wrapper
   * {@link org.trebol.api.models.PersonPojo} object.
   *
   * @param userName The name of the user to fetch data from.
   * @return The personal information of the user.
   * @throws EntityNotFoundException When no user with the provided name exists.
   */
  PersonPojo getProfileFromUserName(String userName) throws EntityNotFoundException;

  /**
   * Updates personal information for a given user.
   *
   * @param userName The name of the user to fetch data from.
   * @return The personal information of the user.
   * @throws EntityNotFoundException When no user with the provided name exists.
   */
  void updateProfileForUserWithName(String userName, PersonPojo profile) throws BadInputException, UserNotFoundException;
}

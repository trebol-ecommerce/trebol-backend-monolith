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

package org.trebol.security.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Provides means to acknowledge the exact permissions of a user.
 */
public interface AuthorizedApiService {

  /**
   * Fetches the REST API routes that the current user can access.
   *
   * @param userDetails An object containing information about the current user.
   * @return A collection of REST API routes in String form.
   */
  Collection<String> getAuthorizedApiRoutes(UserDetails userDetails);

  /**
   * Fetches the operations that the current user can access on a specific REST API route.
   *
   * @param userDetails An object containing information about the current user.
   * @param apiRoute The REST API route to ask about.
   * @return A collection of permissions in String form.
   */
  Collection<String> getAuthorizedApiRouteAccess(UserDetails userDetails, String apiRoute);
}

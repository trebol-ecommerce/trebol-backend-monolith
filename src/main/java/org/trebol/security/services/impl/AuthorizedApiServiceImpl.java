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

package org.trebol.security.services.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.trebol.security.services.AuthorizedApiService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorizedApiServiceImpl
  implements AuthorizedApiService {

  @Override
  public Collection<String> getAuthorizedApiRoutes(UserDetails userDetails) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
    Set<String> resourceRoutes = new HashSet<>();
    for (GrantedAuthority authority : authorities) {
      String resourceAuthority = authority.getAuthority();
      String resourceName = resourceAuthority.replaceAll(":.+$", "");
      resourceRoutes.add(resourceName);
    }

    return resourceRoutes;
  }

  @Override
  public Collection<String> getAuthorizedApiRouteAccess(UserDetails userDetails, String apiRoute) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
    Set<String> authorizedActions = new HashSet<>();
    for (GrantedAuthority authority : authorities) {
      String resourceAuthority = authority.getAuthority();
      if (resourceAuthority.contains(apiRoute)) {
        String actionName = resourceAuthority.replaceAll("^.+:", "");
        authorizedActions.add(actionName);
      }
    }
    return authorizedActions;
  }
}

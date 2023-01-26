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

package org.trebol.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "app_user_role_permissions")
@NoArgsConstructor
@Getter
@Setter
public class UserRolePermission
  implements Serializable {
  private static final long serialVersionUID = 21L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_role_permission_id", nullable = false)
  private Long id;
  @JoinColumn(name = "permission_id", referencedColumnName = "permission_id", updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Permission permission;
  @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id", updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserRole userRole;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserRolePermission that = (UserRolePermission) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(permission, that.permission) &&
      Objects.equals(userRole, that.userRole);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, permission, userRole);
  }

  @Override
  public String toString() {
    return "UserRolePermission{" +
      "id=" + id +
      ", permission=" + permission +
      ", userRole=" + userRole +
      '}';
  }
}

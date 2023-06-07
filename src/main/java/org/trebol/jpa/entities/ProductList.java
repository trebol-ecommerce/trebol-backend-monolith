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

import lombok.*;
import org.trebol.jpa.DBEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "product_lists",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_list_name"}),
        @UniqueConstraint(columnNames = {"product_list_code"}),
    })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductList
    implements DBEntity {
    private static final long serialVersionUID = 16L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_list_id", nullable = false)
    private Long id;
    @Size(min = 2, max = 50)
    @Column(name = "product_list_name", nullable = false)
    private String name;
    @Size(min = 2, max = 25)
    @Column(name = "product_list_code", nullable = false)
    private String code;
    @Column(name = "product_list_disabled", nullable = false)
    private boolean disabled;
    @OneToMany(mappedBy = "list")
    private List<ProductListItem> items;

    /**
     * Please note: this copy-constructor does not include a ProductList's relationship to its items
     *
     * @param source The original Sell
     */
    public ProductList(ProductList source) {
        this.id = source.id;
        this.name = source.name;
        this.code = source.code;
        this.disabled = source.disabled;
        this.items = null;
    }
}

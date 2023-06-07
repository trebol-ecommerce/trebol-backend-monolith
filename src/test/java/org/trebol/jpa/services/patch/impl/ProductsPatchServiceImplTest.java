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

package org.trebol.jpa.services.patch.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.junit.jupiter.api.Assertions.*;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.NOT_ANY;

@ExtendWith(MockitoExtension.class)
class ProductsPatchServiceImplTest {
    @InjectMocks ProductsPatchServiceImpl instance;
    private static ObjectMapper MAPPER;
    private static Product EXISTING_PRODUCT;


    @BeforeAll
    static void beforeAll() {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(NON_NULL);
        EXISTING_PRODUCT = Product.builder()
            .id(1L)
            .barcode(ANY)
            .name(ANY)
            .price(1)
            .description(ANY)
            .stockCurrent(2)
            .stockCritical(1)
            .productCategory(null)
            .build();
    }

    @Test
    void performs_empty_patch() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ProductPojo.builder().build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertEquals(EXISTING_PRODUCT, result);
    }

    @Test
    void patches_name() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ProductPojo.builder()
            .name(NOT_ANY)
            .build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertNotEquals(EXISTING_PRODUCT, result);
        assertEquals(NOT_ANY, result.getName());
    }

    @Test
    void patches_barcode() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ProductPojo.builder()
            .barcode(NOT_ANY)
            .build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertNotEquals(EXISTING_PRODUCT, result);
        assertEquals(NOT_ANY, result.getBarcode());
    }

    @Test
    void patches_price() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ProductPojo.builder()
            .price(3000)
            .build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertNotEquals(EXISTING_PRODUCT, result);
        assertEquals(3000, result.getPrice());
    }

    @Test
    void patches_description() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ProductPojo.builder()
            .description(NOT_ANY)
            .build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertNotEquals(EXISTING_PRODUCT, result);
        assertEquals(NOT_ANY, result.getDescription());
    }

    @Test
    void patches_current_stock() throws BadInputException {
        Integer someStock = 2000;
        Map<String, Object> input = this.mapFrom(ProductPojo.builder()
            .currentStock(someStock)
            .build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertNotEquals(EXISTING_PRODUCT, result);
        assertEquals(someStock, result.getStockCurrent());
    }

    @Test
    void patches_critical_stock() throws BadInputException {
        int someCriticalStock = 100;
        Map<String, Object> input = this.mapFrom(ProductPojo.builder()
            .criticalStock(someCriticalStock)
            .build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertNotEquals(EXISTING_PRODUCT, result);
        assertEquals(someCriticalStock, result.getStockCritical());
    }

    @Test
    void patches_all_fields() throws BadInputException {
        int somePrice = 5000;
        int someStock = 100;
        int someCriticalStock = 10;
        Map<String, Object> input = this.mapFrom(ProductPojo.builder()
            .barcode(NOT_ANY)
            .name(NOT_ANY)
            .price(somePrice)
            .description(NOT_ANY)
            .currentStock(someStock)
            .criticalStock(someCriticalStock)
            .build());
        Product result = instance.patchExistingEntity(input, EXISTING_PRODUCT);
        assertNotEquals(EXISTING_PRODUCT, result);
        assertEquals(NOT_ANY, result.getName());
        assertEquals(NOT_ANY, result.getBarcode());
        assertEquals(somePrice, result.getPrice());
        assertEquals(NOT_ANY, result.getDescription());
        assertEquals(someStock, result.getStockCurrent());
        assertEquals(someCriticalStock, result.getStockCritical());
    }

    @Test
    void does_not_support_old_method_signature() {
        ProductPojo input = ProductPojo.builder().build();
        assertThrows(UnsupportedOperationException.class,
            () -> instance.patchExistingEntity(input, EXISTING_PRODUCT));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapFrom(ProductPojo data) {
        return MAPPER.convertValue(data, Map.class);
    }
}

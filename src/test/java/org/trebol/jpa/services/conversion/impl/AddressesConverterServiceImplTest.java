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

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.api.models.AddressPojo;
import org.trebol.jpa.entities.Address;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.testing.TestConstants.ANY;

class AddressesConverterServiceImplTest {
    AddressesConverterServiceImpl instance;

    @BeforeEach
    void beforeEach() {
        instance = new AddressesConverterServiceImpl();
    }

    @Test
    void converts_to_pojo() {
        Address.AddressBuilder inputBuilder = Address.builder()
            .firstLine(ANY)
            .municipality(ANY)
            .city(ANY)
            .secondLine(null)
            .postalCode(null)
            .notes(null);
        List.of(
            inputBuilder
                .build(),
            inputBuilder
                .secondLine(ANY)
                .build(),
            inputBuilder
                .postalCode(ANY)
                .build(),
            inputBuilder
                .notes(ANY)
                .build()
        ).forEach(input -> {
            AddressPojo result = instance.convertToPojo(input);
            assertEquals(input.getFirstLine(), result.getFirstLine());
            assertEquals(input.getSecondLine(), result.getSecondLine());
            assertEquals(input.getMunicipality(), result.getMunicipality());
            assertEquals(input.getCity(), result.getCity());
            assertEquals(input.getNotes(), result.getNotes());
        });
    }

    @Test
    void converts_to_new_entity() {
        AddressPojo.AddressPojoBuilder inputBuilder = AddressPojo.builder()
            .firstLine(ANY)
            .municipality(ANY)
            .city(ANY)
            .secondLine(null)
            .postalCode(null)
            .notes(null);
        List.of(
            inputBuilder
                .build(),
            inputBuilder
                .secondLine(ANY)
                .build(),
            inputBuilder
                .postalCode(ANY)
                .build(),
            inputBuilder
                .notes(ANY)
                .build()
        ).forEach(input -> {
            Address result = instance.convertToNewEntity(input);
            assertEquals(input.getFirstLine(), result.getFirstLine());
        });
    }
}

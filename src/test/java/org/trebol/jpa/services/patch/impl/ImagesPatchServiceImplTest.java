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

package org.trebol.jpa.services.patch.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ImagePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.testing.ImagesTestHelper;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.NOT_ANY;

@ExtendWith(MockitoExtension.class)
class ImagesPatchServiceImplTest {
    @InjectMocks ImagesPatchServiceImpl instance;
    ImagesTestHelper imagesTestHelper = new ImagesTestHelper();
    private static ObjectMapper MAPPER;
    private static Image EXISTING_IMAGE;

    @BeforeEach
    void beforeEach() {
        imagesTestHelper.resetImages();
    }

    @BeforeAll
    static void beforeAll() {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(NON_NULL);
        EXISTING_IMAGE = Image.builder()
            .code(ANY)
            .filename(ANY)
            .url(ANY)
            .build();
    }

    @Test
    void performs_empty_patch() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ImagePojo.builder().build());
        Image result = instance.patchExistingEntity(input, EXISTING_IMAGE);
        assertEquals(EXISTING_IMAGE, result);
    }

    @Test
    void patches_code() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ImagePojo.builder()
            .code(NOT_ANY)
            .build());
        Image result = instance.patchExistingEntity(input, EXISTING_IMAGE);
        assertNotEquals(EXISTING_IMAGE, result);
        assertEquals(NOT_ANY, result.getCode());
    }

    @Test
    void patches_filename() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ImagePojo.builder()
            .filename(NOT_ANY)
            .build());
        Image result = instance.patchExistingEntity(input, EXISTING_IMAGE);
        assertNotEquals(EXISTING_IMAGE, result);
        assertEquals(NOT_ANY, result.getFilename());
    }

    @Test
    void patches_url() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ImagePojo.builder()
            .url(NOT_ANY)
            .build());
        Image result = instance.patchExistingEntity(input, EXISTING_IMAGE);
        assertNotEquals(EXISTING_IMAGE, result);
        assertEquals(NOT_ANY, result.getUrl());
    }

    @Test
    void patches_all_fields() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ImagePojo.builder()
            .code(NOT_ANY)
            .filename(NOT_ANY)
            .url(NOT_ANY)
            .build());
        Image result = instance.patchExistingEntity(input, EXISTING_IMAGE);
        assertNotEquals(EXISTING_IMAGE, result);
        assertEquals(NOT_ANY, result.getCode());
        assertEquals(NOT_ANY, result.getFilename());
        assertEquals(NOT_ANY, result.getUrl());
    }

    @Test
    void does_not_support_old_method_signature() {
        ImagePojo input = ImagePojo.builder().build();
        assertThrows(UnsupportedOperationException.class,
            () -> instance.patchExistingEntity(input, EXISTING_IMAGE));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapFrom(ImagePojo data) {
        return MAPPER.convertValue(data, Map.class);
    }
}

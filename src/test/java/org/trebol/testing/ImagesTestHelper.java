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

package org.trebol.testing;

import org.trebol.api.models.ImagePojo;
import org.trebol.jpa.entities.Image;

/**
 * Builds & caches reusable instances of Image and ImagePojo
 */
public class ImagesTestHelper {

  public static final long IMAGE_ID = 1L;
  public static final String IMAGE_CODE = "test-img";
  public static final String IMAGE_FILENAME = "testimg.jpg";
  public static final String IMAGE_URL = "http://example.com/img/testimg.jpg";
  private ImagePojo pojoForFetch;
  private ImagePojo pojoBeforeCreation;
  private ImagePojo pojoAfterCreation;
  private Image entityBeforeCreation;
  private Image entityAfterCreation;

  public void resetImages() {
    this.pojoForFetch = null;
    this.pojoBeforeCreation = null;
    this.pojoAfterCreation = null;
    this.entityBeforeCreation = null;
    this.entityAfterCreation = null;
  }

  public ImagePojo imagePojoForFetch() {
    if (this.pojoForFetch == null) {
      this.pojoForFetch = ImagePojo.builder()
        .filename(IMAGE_FILENAME)
        .build();
    }
    return this.pojoForFetch;
  }

  public ImagePojo imagePojoBeforeCreation() {
    if (this.pojoBeforeCreation == null) {
      this.pojoBeforeCreation = ImagePojo.builder()
        .code(IMAGE_CODE)
        .filename(IMAGE_FILENAME)
        .url(IMAGE_URL)
        .build();
    }
    return this.pojoBeforeCreation;
  }

  public ImagePojo imagePojoAfterCreation() {
    if (this.pojoAfterCreation == null) {
      this.pojoAfterCreation = ImagePojo.builder()
        .id(IMAGE_ID)
        .code(IMAGE_CODE)
        .filename(IMAGE_FILENAME)
        .url(IMAGE_URL)
        .build();
    }
    return this.pojoAfterCreation;
  }

  public Image imageEntityBeforeCreation() {
    if (this.entityBeforeCreation == null) {
      this.entityBeforeCreation = Image.builder()
        .code(IMAGE_CODE)
        .filename(IMAGE_FILENAME)
        .url(IMAGE_URL)
        .build();
    }
    return this.entityBeforeCreation;
  }

  public Image imageEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = Image.builder()
        .id(IMAGE_ID)
        .code(IMAGE_CODE)
        .filename(IMAGE_FILENAME)
        .url(IMAGE_URL)
        .build();
    }
    return this.entityAfterCreation;
  }
}

package org.trebol.testhelpers;

import org.trebol.jpa.entities.Image;
import org.trebol.pojo.ImagePojo;

/**
 * Builds & caches reusable instances of Image and ImagePojo
 */
public class ImagesTestHelper {

  public static long IMAGE_ID = 1L;
  public static String IMAGE_CODE = "test-img";
  public static String IMAGE_FILENAME = "testimg.jpg";
  public static String IMAGE_URL = "http://example.com/img/testimg.jpg";
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
      this.pojoForFetch = ImagePojo.builder().filename(IMAGE_FILENAME).build();
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
      this.entityBeforeCreation = new Image(IMAGE_CODE, IMAGE_FILENAME, IMAGE_URL);
    }
    return this.entityBeforeCreation;
  }

  public Image imageEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = new Image(IMAGE_ID, IMAGE_CODE, IMAGE_FILENAME, IMAGE_URL);
    }
    return this.entityAfterCreation;
  }
}

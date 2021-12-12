package org.trebol.jpa.testhelpers;

import org.trebol.jpa.entities.Image;
import org.trebol.pojo.ImagePojo;

public class ImagesJpaCrudServiceTestHelper {

  public static long IMAGE_ID = 1L;
  public static String IMAGE_CODE = "test-img";
  public static String IMAGE_FILENAME = "testimg.jpg";
  public static String IMAGE_URL = "http://example.com/img/testimg.jpg";
  private static ImagePojo pojoForFetch;
  private static ImagePojo pojoBeforeCreation;
  private static ImagePojo pojoAfterCreation;
  private static Image entityBeforeCreation;
  private static Image entityAfterCreation;

  public static void resetImages() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public static ImagePojo imagePojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = new ImagePojo(IMAGE_FILENAME);
    }
    return pojoForFetch;
  }

  public static ImagePojo imagePojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      pojoBeforeCreation = new ImagePojo(IMAGE_CODE, IMAGE_FILENAME, IMAGE_URL);
    }
    return pojoBeforeCreation;
  }

  public static ImagePojo imagePojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = new ImagePojo(IMAGE_ID, IMAGE_CODE, IMAGE_FILENAME, IMAGE_URL);
    }
    return pojoAfterCreation;
  }

  public static Image imageEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      entityBeforeCreation = new Image(IMAGE_CODE, IMAGE_FILENAME, IMAGE_URL);
    }
    return entityBeforeCreation;
  }

  public static Image imageEntityAfterCreation() {
    if (entityAfterCreation == null) {
      entityAfterCreation = new Image(IMAGE_ID, IMAGE_CODE, IMAGE_FILENAME, IMAGE_URL);
    }
    return entityAfterCreation;
  }
}

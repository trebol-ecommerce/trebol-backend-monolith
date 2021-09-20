package org.trebol.exceptions;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class CorsMappingParseException
    extends Exception {

  private final String corsMapping;

  public CorsMappingParseException(String corsMapping) {
    super();
    this.corsMapping = corsMapping;
  }

  @Override
  public String getMessage() {
    return "Could not parse CORS mapping. Format must be 'METHODS /path', provided value was '" + corsMapping + "'";
  }

}

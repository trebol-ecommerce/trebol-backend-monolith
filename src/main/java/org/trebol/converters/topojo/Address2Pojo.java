package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.AddressPojo;
import org.trebol.jpa.entities.Address;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Address2Pojo
    implements Converter<Address, AddressPojo> {

  @Override
  public AddressPojo convert(Address source) {
    AddressPojo target = new AddressPojo();
    target.setCity(source.getCity());
    target.setMunicipality(source.getMunicipality());
    target.setFirstLine(source.getFirstLine());
    if (source.getSecondLine() != null && !source.getSecondLine().isBlank()) {
      target.setSecondLine(source.getSecondLine());
    }
    if (source.getPostalCode() != null && !source.getPostalCode().isBlank()) {
      target.setPostalCode(source.getPostalCode());
    }
    if (source.getNotes() != null && !source.getNotes().isBlank()) {
      target.setNotes(source.getNotes());
    }
    return target;
  }
}

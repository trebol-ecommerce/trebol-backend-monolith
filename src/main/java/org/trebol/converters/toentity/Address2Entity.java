/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trebol.jpa.entities.Address;
import org.trebol.pojo.AddressPojo;

@Component
public class Address2Entity
    implements Converter<AddressPojo, Address> {

  @Override
  public Address convert(AddressPojo source) {
    Address target = new Address();
    target.setFirstLine(source.getFirstLine());
    target.setCity(source.getCity());
    target.setMunicipality(source.getMunicipality());
    if (source.getPostalCode() != null) {
      target.setPostalCode(source.getPostalCode());
    }
    if (source.getNotes()!= null) {
      target.setNotes(source.getNotes());
    }
    return target;
  }
}

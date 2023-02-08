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

package org.trebol.api.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.models.ReceiptPojo;
import org.trebol.api.services.ReceiptService;
import org.trebol.common.exceptions.BadInputException;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/public/receipt")
public class PublicReceiptController {
  private final ReceiptService receiptService;

  @Autowired
  public PublicReceiptController(
    ReceiptService receiptService
  ) {
    this.receiptService = receiptService;
  }

  @GetMapping({"/{token}", "/{token}/"})
  public ReceiptPojo fetchReceiptById(@PathVariable("token") String token)
    throws BadInputException, EntityNotFoundException {
    if (StringUtils.isBlank(token)) {
      throw new BadInputException("An incorrect receipt token was provided");
    }
    return this.receiptService.fetchReceiptByTransactionToken(token);
  }
}

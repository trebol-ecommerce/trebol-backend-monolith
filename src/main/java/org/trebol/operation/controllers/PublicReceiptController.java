package org.trebol.operation.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.pojo.ReceiptPojo;
import org.trebol.operation.IReceiptService;

import javassist.NotFoundException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/public/receipt")
public class PublicReceiptController {
  private final Logger LOG = LoggerFactory.getLogger(PublicReceiptController.class);

  private final IReceiptService receiptService;

  @Autowired
  public PublicReceiptController(IReceiptService receiptService) {
    this.receiptService = receiptService;
  }

  @GetMapping({"/{token}", "/{token}/"})
  public ReceiptPojo fetchReceiptById(@PathVariable("token") String token)
    throws NotFoundException {
    if (token == null) {
      throw new RuntimeException("An incorrect receipt token was provided");
    }
    return this.receiptService.fetchReceiptByTransactionToken(token);
  }
}

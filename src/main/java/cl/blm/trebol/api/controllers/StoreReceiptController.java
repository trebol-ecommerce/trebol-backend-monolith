package cl.blm.trebol.api.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.api.pojo.ReceiptPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/store/receipt")
public class StoreReceiptController {
  private final Logger LOG = LoggerFactory.getLogger(StoreReceiptController.class);

//  private final ReceiptService receiptService;

  public StoreReceiptController() {
  }

  @GetMapping("/{id}")
  public ReceiptPojo fetchReceiptById(@PathVariable("id") Integer id) {
    throw new UnsupportedOperationException("Not implemented");
  }
}

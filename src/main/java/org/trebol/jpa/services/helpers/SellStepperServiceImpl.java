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

package org.trebol.jpa.services.helpers;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.services.ISellStepperJpaService;

import java.util.Optional;

@Transactional
@Service
public class SellStepperServiceImpl
  implements ISellStepperJpaService {

  private final Logger logger = LoggerFactory.getLogger(SellStepperServiceImpl.class);
  private final ISalesJpaRepository salesRepository;
  private final ISellStatusesJpaRepository statusesRepository;

  @Autowired
  public SellStepperServiceImpl(ISalesJpaRepository salesRepository,
                                ISellStatusesJpaRepository statusesRepository) {
    this.salesRepository = salesRepository;
    this.statusesRepository = statusesRepository;
  }

  public void setSellStatusToPaymentStartedWithToken(Long id, String token) throws NotFoundException {
    this.setSellStatusByName(id, "Payment Started");
    salesRepository.setTransactionToken(id, token);
  }

  public void setSellStatusToPaymentAborted(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Payment Cancelled");
  }

  public void setSellStatusToPaymentFailed(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Payment Failed");
  }

  public void setSellStatusToPaidUnconfirmed(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Paid, Unconfirmed");
  }

  public void setSellStatusToPaidConfirmed(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Paid, Confirmed");
  }

  public void setSellStatusToRejected(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Rejected");
  }

  public void setSellStatusToCompleted(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Delivery Complete");
  }

  private void setSellStatusByName(Long sellId, String statusName) throws NotFoundException {
    if (!salesRepository.existsById(sellId)) {
      throw new NotFoundException("The specified sell does not exist");
    } else {
      Optional<SellStatus> statusEntityByName = statusesRepository.findByName(statusName);
      if (statusEntityByName.isPresent()) {
        SellStatus statusEntity = statusEntityByName.get();
        Integer statusChangeResponse = salesRepository.setStatus(sellId, statusEntity);
        logger.debug("statusChangeResponse={}", statusChangeResponse);
      } else {
        logger.error("No sell status exists by the name '{}'", statusName);
      }
    }
  }
}

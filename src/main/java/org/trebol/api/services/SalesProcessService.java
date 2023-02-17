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

package org.trebol.api.services;

import org.trebol.api.models.SellPojo;
import org.trebol.common.exceptions.BadInputException;

import javax.persistence.EntityNotFoundException;

/**
 * Declares methods to advance through steps of transaction.
 */
public interface SalesProcessService {

  /**
   * Updates status of a sell to "started", meaning its bill must be paid.
   *
   * @param sell The sell whose status will be updated
   * @throws BadInputException       When the transaction is not in "pending" state, or when it doesn't have a token
   * @throws EntityNotFoundException When the transaction is not found in the persistence context
   */
  SellPojo markAsStarted(SellPojo sell) throws BadInputException, EntityNotFoundException;

  /**
   * Updates status of a sell to "aborted", meaning nothing more can be done about it.
   *
   * @param sell The sell whose status will be updated
   * @throws BadInputException       When the transaction is not in "started" state
   * @throws EntityNotFoundException When the transaction is not found in the persistence context
   */
  SellPojo markAsAborted(SellPojo sell) throws BadInputException, EntityNotFoundException;

  /**
   * Updates status of a sell to "failed", meaning nothing more can be done about it.
   *
   * @param sell The sell whose status will be updated
   * @throws BadInputException       When the transaction is not in "started" state
   * @throws EntityNotFoundException When the transaction is not found in the persistence context
   */
  SellPojo markAsFailed(SellPojo sell) throws BadInputException, EntityNotFoundException;

  /**
   * Updates status of a sell to "paid/unconfirmed", meaning it can be "rejected" or "confirmed" afterwards.
   * This must be only possible right after the sell has been created.
   *
   * @param sell The sell whose status will be updated
   * @throws BadInputException       When the transaction is not in "started" state
   * @throws EntityNotFoundException When the transaction is not found in the persistence context
   */
  SellPojo markAsPaid(SellPojo sell) throws BadInputException, EntityNotFoundException;

  /**
   * Updates status of a sell to "confirmed", meaning it must be eventually delivered, or fail to do so.
   *
   * @param sell The sell whose status will be updated
   * @throws BadInputException       When the transaction is not in "paid/unconfirmed" state
   * @throws EntityNotFoundException When the transaction is not found in the persistence context
   */
  SellPojo markAsConfirmed(SellPojo sell) throws BadInputException, EntityNotFoundException;

  /**
   * Updates status of a sell to "rejected", meaning a refund must be issued to the customer.
   * This must be only possible after payment is completed, notified and saved in the application state.
   *
   * @throws BadInputException       When the transaction is not "paid/unconfirmed" state
   * @throws EntityNotFoundException When the transaction is not found in the persistence context
   */
  SellPojo markAsRejected(SellPojo sell) throws BadInputException, EntityNotFoundException;

  /**
   * Updates status of a sell to "completed", meaning the whole process is finished and the customer is contempt.
   *
   * @param sell The sell whose status will be updated
   * @throws BadInputException       When the transaction is not in "confirmed" state
   * @throws EntityNotFoundException When the transaction is not found in the persistence context
   */
  SellPojo markAsCompleted(SellPojo sell) throws BadInputException, EntityNotFoundException;
}

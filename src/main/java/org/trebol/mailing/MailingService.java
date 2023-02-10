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

package org.trebol.mailing;

import org.trebol.api.models.SellPojo;

/**
 * Point of entry for services to send mail to customers and owners alike
 */
public interface MailingService {
  /**
   * Generate and send an e-mail to the customer, regarding an update on their transaction' status.<br/>
   * Should support all transaction stages
   *
   * @param sell The transaction metadata
   * @throws MailingServiceException When any error occurs while interacting with the mail server/service provider
   */
  void notifyOrderStatusToClient(SellPojo sell) throws MailingServiceException;

  /**
   * Generate and send an e-mail to store owners, regarding an update on a certain transaction' status.<br/>
   * It is not mandatory to support all transaction stages; owners may only need to be aware of some events.
   *
   * @param sell The transaction metadata
   * @throws MailingServiceException When any error occurs while interacting with the mail server/service provider
   */
  void notifyOrderStatusToOwners(SellPojo sell) throws MailingServiceException;
}

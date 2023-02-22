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

package org.trebol.config;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * A temporary helper class to hold on to some name keys
 */
@NoArgsConstructor(access = PRIVATE)
public final class Constants {
  public static final String SELL_STATUS_PENDING = "Pending";
  public static final String SELL_STATUS_PAYMENT_STARTED = "Payment Started";
  public static final String SELL_STATUS_PAYMENT_CANCELLED = "Payment Cancelled";
  public static final String SELL_STATUS_PAYMENT_FAILED = "Payment Failed";
  public static final String SELL_STATUS_PAID_UNCONFIRMED = "Paid, Unconfirmed";
  public static final String SELL_STATUS_PAID_CONFIRMED = "Paid, Confirmed";
  public static final String SELL_STATUS_REJECTED = "Rejected";
  public static final String SELL_STATUS_COMPLETED = "Delivery Complete";
  public static final String BILLING_TYPE_INDIVIDUAL = "Bill";
  public static final String BILLING_TYPE_ENTERPRISE = "Enterprise Invoice";
  public static final String WEBPAY_SUCCESS_TOKEN_HEADER_NAME = "token_ws";
  public static final String WEBPAY_ABORTION_TOKEN_HEADER_NAME = "TBK_TOKEN";
  public static final String AUTHORITY_CHECKOUT = "checkout";
  public static final String JWT_CLAIM_AUTHORITIES = "authorities";
  public static final String JWT_PREFIX = "Bearer ";
}

/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
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

package org.trebol.jpa.services.patch.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.OrderPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Order;
import org.trebol.jpa.repositories.PaymentTypesRepository;
import org.trebol.jpa.repositories.OrderStatusesRepository;
import org.trebol.jpa.repositories.ShippersRepository;
import org.trebol.jpa.services.patch.OrdersPatchService;

import java.time.Instant;
import java.util.Map;

@Transactional
@Service
public class OrdersPatchServiceImpl
    implements OrdersPatchService {
    private final OrderStatusesRepository statusesRepository;
    private final PaymentTypesRepository paymentTypesRepository;
    private final ShippersRepository shippersRepository;

    @Autowired
    public OrdersPatchServiceImpl(
        OrderStatusesRepository statusesRepository,
        PaymentTypesRepository paymentTypesRepository,
        ShippersRepository shippersRepository
    ) {
        this.statusesRepository = statusesRepository;
        this.paymentTypesRepository = paymentTypesRepository;
        this.shippersRepository = shippersRepository;
    }

    @Transactional
    @Override
    public Order patchExistingEntity(Map<String, Object> changes, Order existing) throws BadInputException {
        Order target = new Order(existing);

        try {
            if (changes.containsKey("date")) {
                String rawDate = (String) changes.get("date");
                if (rawDate!=null) {
                    Instant date = Instant.parse(rawDate);
                    target.setDate(date);
                }
            }

            if (changes.containsKey("status")) {
                String statusName = (String) changes.get("status");
                if (!StringUtils.isBlank(statusName)) {
                    statusesRepository.findByName(statusName).ifPresent(target::setStatus);
                }
            }

            if (changes.containsKey("paymentType")) {
                String paymentTypeName = (String) changes.get("paymentType");
                if (!StringUtils.isBlank(paymentTypeName)) {
                    paymentTypesRepository.findByName(paymentTypeName).ifPresent(target::setPaymentType);
                }
            }

            if (changes.containsKey("shipper")) {
                String shipperName = (String) changes.get("shipper");
                if (!StringUtils.isBlank(shipperName)) {
                    shippersRepository.findByName(shipperName).ifPresent(target::setShipper);
                }
            }
        } catch (ClassCastException ex) {
            throw new BadInputException("The ");
        }

        return target;
    }

    @Override
    public Order patchExistingEntity(OrderPojo changes, Order existing) throws BadInputException {
        throw new UnsupportedOperationException("This method signature has been deprecated");
    }
}

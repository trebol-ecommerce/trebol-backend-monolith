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

package org.trebol.jpa.services.crud.impl;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.AddressPojo;
import org.trebol.api.models.OrderDetailPojo;
import org.trebol.api.models.OrderPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.config.ApiProperties;
import org.trebol.jpa.entities.Order;
import org.trebol.jpa.repositories.OrdersRepository;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.OrdersConverterService;
import org.trebol.jpa.services.crud.CrudGenericService;
import org.trebol.jpa.services.crud.OrdersCrudService;
import org.trebol.jpa.services.patch.OrdersPatchService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrdersCrudServiceImpl
    extends CrudGenericService<OrderPojo, Order>
    implements OrdersCrudService {
    private final OrdersRepository ordersRepository;
    private final OrdersConverterService ordersConverterService;
    private final AddressesConverterService addressesConverterService;
    private final ApiProperties apiProperties;

    @Autowired
    public OrdersCrudServiceImpl(
        OrdersRepository ordersRepository,
        OrdersConverterService ordersConverterService,
        OrdersPatchService ordersPatchService,
        AddressesConverterService addressesConverterService,
        ApiProperties apiProperties
    ) {
        super(ordersRepository, ordersConverterService, ordersPatchService);
        this.ordersRepository = ordersRepository;
        this.ordersConverterService = ordersConverterService;
        this.addressesConverterService = addressesConverterService;
        this.apiProperties = apiProperties;
    }

    @Override
    public Optional<Order> getExisting(OrderPojo input) {
        Long buyOrder = input.getBuyOrder();
        if (buyOrder==null) {
            return Optional.empty();
        } else {
            return this.ordersRepository.findById(buyOrder);
        }
    }

    @Override
    public OrderPojo readOne(Predicate conditions) throws EntityNotFoundException {
        Optional<Order> matchingSell = ordersRepository.findOne(conditions);
        if (matchingSell.isPresent()) {
            Order found = matchingSell.get();
            OrderPojo target = ordersConverterService.convertToPojo(found);

            AddressPojo billingAddress = addressesConverterService.convertToPojo(found.getBillingAddress());
            target.setBillingAddress(billingAddress);

            if (found.getShippingAddress()!=null) {
                AddressPojo shippingAddress = addressesConverterService.convertToPojo(found.getShippingAddress());
                target.setShippingAddress(shippingAddress);
            }

            List<OrderDetailPojo> details = found.getDetails().stream()
                .map(ordersConverterService::convertDetailToPojo)
                .collect(Collectors.toList());
            target.setDetails(details);

            return target;
        } else {
            throw new EntityNotFoundException("No sell matches the filtering conditions");
        }
    }

    @Override
    protected Order flushPartialChanges(Map<String, Object> changes, Order existingEntity) throws BadInputException {
        Integer statusCode = existingEntity.getStatus().getCode();
        if ((statusCode >= 3 || statusCode < 0) && !apiProperties.isAbleToEditOrdersAfterBeingProcessed()) {
            throw new BadInputException("The requested transaction cannot be modified");
        }
        return super.flushPartialChanges(changes, existingEntity);
    }
}

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

package org.trebol.jpa.services.crud.impl;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.AddressPojo;
import org.trebol.api.models.SellDetailPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.config.ApiProperties;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.SalesRepository;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.SalesConverterService;
import org.trebol.jpa.services.crud.CrudGenericService;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.jpa.services.patch.SalesPatchService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class SalesCrudServiceImpl
    extends CrudGenericService<SellPojo, Sell>
    implements SalesCrudService {
    private final SalesRepository salesRepository;
    private final SalesConverterService salesConverterService;
    private final AddressesConverterService addressesConverterService;
    private final ApiProperties apiProperties;

    @Autowired
    public SalesCrudServiceImpl(
        SalesRepository salesRepository,
        SalesConverterService salesConverterService,
        SalesPatchService salesPatchService,
        AddressesConverterService addressesConverterService,
        ApiProperties apiProperties
    ) {
        super(salesRepository, salesConverterService, salesPatchService);
        this.salesRepository = salesRepository;
        this.salesConverterService = salesConverterService;
        this.addressesConverterService = addressesConverterService;
        this.apiProperties = apiProperties;
    }

    @Override
    public Optional<Sell> getExisting(SellPojo input) {
        Long buyOrder = input.getBuyOrder();
        if (buyOrder==null) {
            return Optional.empty();
        } else {
            return this.salesRepository.findById(buyOrder);
        }
    }

    @Override
    public SellPojo readOne(Predicate conditions) throws EntityNotFoundException {
        Optional<Sell> matchingSell = salesRepository.findOne(conditions);
        if (matchingSell.isPresent()) {
            Sell found = matchingSell.get();
            SellPojo target = salesConverterService.convertToPojo(found);

            AddressPojo billingAddress = addressesConverterService.convertToPojo(found.getBillingAddress());
            target.setBillingAddress(billingAddress);

            if (found.getShippingAddress()!=null) {
                AddressPojo shippingAddress = addressesConverterService.convertToPojo(found.getShippingAddress());
                target.setShippingAddress(shippingAddress);
            }

            List<SellDetailPojo> details = found.getDetails().stream()
                .map(salesConverterService::convertDetailToPojo)
                .collect(Collectors.toList());
            target.setDetails(details);

            return target;
        } else {
            throw new EntityNotFoundException("No sell matches the filtering conditions");
        }
    }

    @Override
    protected Sell flushPartialChanges(Map<String, Object> changes, Sell existingEntity) throws BadInputException {
        Integer statusCode = existingEntity.getStatus().getCode();
        if ((statusCode >= 3 || statusCode < 0) && !apiProperties.isAbleToEditSalesAfterBeingProcessed()) {
            throw new BadInputException("The requested transaction cannot be modified");
        }
        return super.flushPartialChanges(changes, existingEntity);
    }
}

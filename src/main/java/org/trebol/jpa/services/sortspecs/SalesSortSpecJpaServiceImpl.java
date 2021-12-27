package org.trebol.jpa.services.sortspecs;

import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QSell;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericSortSpecJpaService;

import java.util.Map;

@Service
public class SalesSortSpecJpaServiceImpl
  extends GenericSortSpecJpaService<Sell> {

  public SalesSortSpecJpaServiceImpl() {
    super(Map.of("buyOrder", QSell.sell.id.asc(),
                 "date", QSell.sell.date.asc(),
                 "status", QSell.sell.status.code.asc(),
                 "customer", QSell.sell.customer.person.lastName.asc(),
                 "shipper", QSell.sell.shipper.name.asc(),
                 "totalValue", QSell.sell.totalValue.asc(),
                 "netValue", QSell.sell.netValue.asc(),
                 "totalItems", QSell.sell.totalItems.asc(),
                 "transportValue", QSell.sell.transportValue.asc()));
  }

  @Override
  public QSell getBasePath() { return QSell.sell; }
}

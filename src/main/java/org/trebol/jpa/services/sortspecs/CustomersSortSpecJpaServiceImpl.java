package org.trebol.jpa.services.sortspecs;

import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.QCustomer;
import org.trebol.jpa.services.GenericSortSpecJpaService;

import java.util.Map;

@Service
public class CustomersSortSpecJpaServiceImpl
  extends GenericSortSpecJpaService<Customer> {

  public CustomersSortSpecJpaServiceImpl() {
    super(Map.of("idNumber",  QCustomer.customer.person.idNumber.asc(),
                 "firstName", QCustomer.customer.person.firstName.asc(),
                 "email",     QCustomer.customer.person.email.asc(),
                 "phone1",    QCustomer.customer.person.phone1.asc(),
                 "phone2",    QCustomer.customer.person.phone2.asc(),
                 "name",      QCustomer.customer.person.lastName.asc(),
                 "lastName",  QCustomer.customer.person.lastName.asc()));
  }

  @Override
  public QCustomer getBasePath() { return QCustomer.customer; }
}

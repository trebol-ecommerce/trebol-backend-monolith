package org.trebol.jpa.services.sortspecs;

import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QSalesperson;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.services.GenericSortSpecJpaService;

import java.util.Map;

@Service
public class SalespeopleSortSpecJpaServiceImpl
  extends GenericSortSpecJpaService<Salesperson> {

  public SalespeopleSortSpecJpaServiceImpl() {
    super(Map.of("idNumber",  QSalesperson.salesperson.person.idNumber.asc(),
                 "firstName", QSalesperson.salesperson.person.firstName.asc(),
                 "email",     QSalesperson.salesperson.person.email.asc(),
                 "phone1",    QSalesperson.salesperson.person.phone1.asc(),
                 "phone2",    QSalesperson.salesperson.person.phone2.asc(),
                 "name",      QSalesperson.salesperson.person.lastName.asc(),
                 "lastName",  QSalesperson.salesperson.person.lastName.asc()));
  }

  @Override
  public QSalesperson getBasePath() { return QSalesperson.salesperson; }
}

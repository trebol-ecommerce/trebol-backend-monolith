package org.trebol.jpa.repositories;

import org.springframework.stereotype.Repository;

import org.trebol.jpa.GenericRepository;
import org.trebol.jpa.entities.ProductFamily;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ProductFamiliesRepository
    extends GenericRepository<ProductFamily, Integer> {

}

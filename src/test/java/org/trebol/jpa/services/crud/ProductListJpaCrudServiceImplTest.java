package org.trebol.jpa.services.crud;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.repositories.IProductListsJpaRepository;
import org.trebol.pojo.ProductListPojo;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductListJpaCrudServiceImplTest {
	@InjectMocks ProductListJpaCrudServiceImpl instance;
	@Mock	IProductListsJpaRepository productListRepository;
	@Mock	IProductListItemsJpaRepository productListItemRepository;
	
	@Test
	void delete_whenProductListNotFound_throwsEntityNotFoundException() {
		
		when(productListRepository.count(any(Predicate.class))).thenReturn(0L);
		
		assertThrows(EntityNotFoundException.class, () -> instance.delete(new BooleanBuilder()));
	}
	
	@Test
	void delete_whenProductListFound_shouldCallDeleteOnRepositories() {
		ProductList productListMock = new ProductList();
		productListMock.setId(1L);
		List<ProductList> productListsMock = List.of(productListMock);
		when(productListRepository.count(any(Predicate.class))).thenReturn(1L);
		when(productListRepository.findAll(any(Predicate.class))).thenReturn(productListsMock);
		
		instance.delete(new BooleanBuilder());
		
		verify(productListItemRepository, times(productListsMock.size())).deleteByListId(1L);
		verify(productListRepository).deleteAll(productListsMock);
	}
	
	@Test
	void getExisting_whenIdNull_shouldReturnEmptyOptional() {
		ProductListPojo productListPojoMock = ProductListPojo.builder()
				.id(null)
				.name("productListPojoName")
				.build();
		
		Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);
		
		assertTrue(actualProductListOptional.isEmpty());
	}
	
	@Test
	void getExisting_whenNameNull_shouldReturnEmptyOptional() {
		ProductListPojo productListPojoMock = ProductListPojo.builder().build();
		
		Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);
		
		assertTrue(actualProductListOptional.isEmpty());
	}
	
	@Test
	void getExisting_whenNameNotNull_shouldReturnProductList() {
		ProductListPojo productListPojoMock = ProductListPojo.builder()
				.name("productListPojoName")
				.build();
		ProductList productListMock = new ProductList();
		productListMock.setName("productListName");
		when(productListRepository.findByName(anyString())).thenReturn(Optional.of(productListMock));
		
		Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);

    verify(productListRepository).findByName(productListPojoMock.getName());
		assertTrue(actualProductListOptional.isPresent());
		assertEquals(productListMock, actualProductListOptional.get());
	}	

}

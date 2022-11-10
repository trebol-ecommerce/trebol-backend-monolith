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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductListJpaCrudServiceImplTest {
	@InjectMocks	ProductListJpaCrudServiceImpl instance;
	@Mock	IProductListsJpaRepository productListRepository;
	@Mock	IProductListItemsJpaRepository productListItemRepository;
	
	@Test
	void delete_whenProductListNotFound_throwsEntityNotFoundException() {
		Predicate predicateMock = new BooleanBuilder();
		
		when(productListRepository.count(predicateMock)).thenReturn(0L);
		
		assertThrows(EntityNotFoundException.class, () -> instance.delete(predicateMock));		
	}
	
	@Test
	void delete_whenProductListFound_shouldCallDeleteOnRepositories() {
		Predicate predicateMock = new BooleanBuilder();
		
		ProductList productListMock = new ProductList();
		productListMock.setId(1L);
		List<ProductList> productListsMock = List.of(productListMock);
		
		when(productListRepository.count(predicateMock)).thenReturn(1L);
		when(productListRepository.findAll(predicateMock)).thenReturn(productListsMock);
		
		instance.delete(predicateMock);
		
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
		
		assertEquals(Optional.empty(), actualProductListOptional);
	}
	
	@Test
	void getExisting_whenNameNull_shouldReturnEmptyOptional() {
		ProductListPojo productListPojoMock = ProductListPojo.builder()
				.id(1L)
				.name(null)
				.build();
		
		Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);
		
		assertEquals(Optional.empty(), actualProductListOptional);
	}
	
	@Test
	void getExisting_whenIdAndNameNotNull_shouldReturnProductList() {
		ProductListPojo productListPojoMock = ProductListPojo.builder()
				.id(1L)
				.name("productListPojoName")
				.build();
		
		ProductList productListMock = new ProductList();
		productListMock.setName("productListName");
		
		when(productListRepository.findById(anyLong())).thenReturn(Optional.of(productListMock));
		
		Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);
		
		assertTrue(actualProductListOptional.isPresent());
		assertEquals(productListMock, actualProductListOptional.get());
	}	

}

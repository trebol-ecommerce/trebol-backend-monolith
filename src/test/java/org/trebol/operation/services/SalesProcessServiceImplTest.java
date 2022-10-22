package org.trebol.operation.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.SELL_STATUS_PAYMENT_STARTED;
import static org.trebol.config.Constants.SELL_STATUS_PENDING;
import static org.trebol.config.Constants.SELL_STATUS_PAYMENT_CANCELLED;
import static org.trebol.config.Constants.SELL_STATUS_PAYMENT_FAILED;
import static org.trebol.config.Constants.SELL_STATUS_PAID_UNCONFIRMED;
import static org.trebol.config.Constants.SELL_STATUS_PAID_CONFIRMED;
import static org.trebol.config.Constants.SELL_STATUS_REJECTED;
import static org.trebol.config.Constants.SELL_STATUS_COMPLETED;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.repositories.ISellDetailsJpaRepository;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellPojo;

@ExtendWith(MockitoExtension.class)
class SalesProcessServiceImplTest {
	
	@InjectMocks	SalesProcessServiceImpl instance;
	@Mock	GenericCrudJpaService<SellPojo, Sell> crudService;
	@Mock	ISalesJpaRepository salesRepository;
	@Mock	ITwoWayConverterJpaService<SellPojo, Sell> converterService;	
	@Mock	ISellStatusesJpaRepository sellStatusesRepository;
	
	@Mock	ISellDetailsJpaRepository sellDetailsRepository;	
	//@Mock	ProductsConverterJpaServiceImpl productConverterService;
	
	@Nested
	class MarkAsStarted {	
		
		@Test
		void markAsStarted_SellStatus_IsNotPending_BadInputException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName("status");
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock));
			
			assertThrows(BadInputException.class, () -> instance.markAsStarted(sellPojoMock));
		}
		
		@Test
		void markAsStarted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
			// Setup mock objects				
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PENDING);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException	
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertThrows(IllegalStateException.class, () -> instance.markAsStarted(sellPojoMock));		
		}
		
		@Test
		void markAsStarted_ShouldReturn_SellPojo_WithStatusStarted() throws BadInputException {
			// Setup mock objects				
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PENDING);
						
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException	
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
			when(converterService.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException			
			
			assertEquals(SELL_STATUS_PAYMENT_STARTED, instance.markAsStarted(sellPojoMock).getStatus());
		}
	}
	
	@Nested
	class MarkAsAborted {
		
		@Test
		void markAsAborted_SellStatus_IsNotStarted_BadInputException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName("status");
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
			
			assertThrows(BadInputException.class, () -> instance.markAsAborted(sellPojoMock));
		}
		
		@Test
		void markAsAborted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertThrows(IllegalStateException.class, () -> instance.markAsAborted(sellPojoMock));
		}
		
		@Test
		void markAsAborted_ShouldReturn_SellPojo_WithStatusCancelled() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();			
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);
			
			Sell sellMock = new Sell();			
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException	
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
			when(converterService.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
			
			assertEquals(SELL_STATUS_PAYMENT_CANCELLED, instance.markAsAborted(sellPojoMock).getStatus());
		}
	}
	
	@Nested
	class MarkAsFailed {
		
		@Test
		void markAsFailed_SellStatus_IsNotStarted_BadInputException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName("status");
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
			
			assertThrows(BadInputException.class, () -> instance.markAsFailed(sellPojoMock));
		}
		
		@Test
		void markAsFailed_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertThrows(IllegalStateException.class, () -> instance.markAsFailed(sellPojoMock));
		}
		
		@Test
		void markAsFailed_ShouldReturn_SellPojo_WithStatusFailed() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();		
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));			
			when(converterService.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
					
			assertEquals(SELL_STATUS_PAYMENT_FAILED, instance.markAsFailed(sellPojoMock).getStatus());
		}
	}
	
	@Nested
	class MarkAsPaid {
		
		@Test
		void markAsPaid__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();		
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName("status");
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
			
			assertThrows(BadInputException.class, () -> instance.markAsPaid(sellPojoMock));
		}
		
		@Test
		void markAsPaid_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertThrows(IllegalStateException.class, () -> instance.markAsPaid(sellPojoMock));
		}
		
		@Test
		void markAsPaid_ShouldReturn_SellPojo_WithStatusUnconfirmed() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();			
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));			
			when(converterService.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
					
			assertEquals(SELL_STATUS_PAID_UNCONFIRMED, instance.markAsPaid(sellPojoMock).getStatus());
		}		
	}
	
	@Nested
	class MarkAsConfirmed {
		
		@Test
		void markAsConfirmed__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();		
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName("status");
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
			
			assertThrows(BadInputException.class, () -> instance.markAsConfirmed(sellPojoMock));
		}
		
		@Test
		void markAsConfirmed_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertThrows(IllegalStateException.class, () -> instance.markAsConfirmed(sellPojoMock));
		}
		
		@Test
		void markAsConfirmed_ShouldReturn_SellPojo_WithStatusConfirmed() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();		
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));				
			when(converterService.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
						
			assertEquals(SELL_STATUS_PAID_CONFIRMED, instance.markAsConfirmed(sellPojoMock).getStatus());
		}		
	}
	
	@Nested
	class MarkAsRejected {
		
		@Test
		void markAsRejected__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();		
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName("status");
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
			
			assertThrows(BadInputException.class, () -> instance.markAsRejected(sellPojoMock));
		}
		
		@Test
		void markAsRejected_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertThrows(IllegalStateException.class, () -> instance.markAsRejected(sellPojoMock));
		}
		
		@Test
		void markAsRejected_ShouldReturn_SellPojo_WithStatusRejected() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();			
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));			
			when(converterService.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
						
			assertEquals(SELL_STATUS_REJECTED, instance.markAsRejected(sellPojoMock).getStatus());
		}		
	}
	
	@Nested
	class MarkAsCompleted {
		
		@Test
		void markAsCompleted__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();		
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName("status");
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
			
			assertThrows(BadInputException.class, () -> instance.markAsCompleted(sellPojoMock));
		}
		
		@Test
		void markAsCompleted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAID_CONFIRMED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertThrows(IllegalStateException.class, () -> instance.markAsCompleted(sellPojoMock));
		}
		
		@Test
		void markAsCompleted_ShouldReturn_SellPojo_WithStatusCompleted() throws BadInputException {
			// Setup mock objects
			SellPojo sellPojoMock = SellPojo.builder().build();	
			
			SellStatus sellStatusMock = new SellStatus();
			sellStatusMock.setName(SELL_STATUS_PAID_CONFIRMED);
			
			Sell sellMock = new Sell();
			sellMock.setStatus(sellStatusMock);
			
			// Stubbing
			when(crudService.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException		
			when(sellStatusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
			when(converterService.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
					
			assertEquals(SELL_STATUS_COMPLETED, instance.markAsCompleted(sellPojoMock).getStatus());
		}
	}
}

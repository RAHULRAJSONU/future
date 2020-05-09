package com.rahul.futures;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository customerRepo;
    private CustomerService customerService;

    @BeforeEach
    void setup() {
        customerRepo = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepo);
    }

    @Test
    void addCustomer_shouldAddANewCustomerToTheRepo() {
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        Customer expectedCustomer = TestHelpers.generateCustomer();

        when(customerRepo.saveCustomer(any())).thenReturn(CompletableFuture.completedFuture(null));

        UUID customerId = customerService.addCustomer(
                expectedCustomer.getFirstName(),
                expectedCustomer.getLastName(),
                expectedCustomer.getAddress(),
                expectedCustomer.getPhoneNumber()
        ).join();

        verify(customerRepo).saveCustomer(captor.capture());

        Customer actualCustomer = captor.getValue();

        assertEquals(customerId, actualCustomer.getId());
        assertEquals(expectedCustomer.getFirstName(), actualCustomer.getFirstName());
        assertEquals(expectedCustomer.getLastName(), actualCustomer.getLastName());
        assertEquals(expectedCustomer.getAddress(), actualCustomer.getAddress());
        assertEquals(expectedCustomer.getPhoneNumber(), actualCustomer.getPhoneNumber());
    }

    @Test
    void getCustomerFirstName_shouldExtractTheFirstNameFromTheCustomer() {
        Customer customer = TestHelpers.generateCustomer();

        when(customerRepo.getCustomer(customer.getId()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(customer)));

        Optional<String> result = customerService.getCustomerFirstName(customer.getId()).join();

        assertTrue(result.isPresent());
        assertEquals(customer.getFirstName(), result.get());
    }

    @Test
    void getCustomerLastName_shouldExtractTheLastNameFromTheCustomer() {
        Customer customer = TestHelpers.generateCustomer();

        when(customerRepo.getCustomer(customer.getId()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(customer)));

        Optional<String> result = customerService.getCustomerLastName(customer.getId()).join();

        assertTrue(result.isPresent());
        assertEquals(customer.getLastName(), result.get());
    }

    @Test
    void getCustomerAddress_shouldExtractTheAddressFromTheCustomer() {
        Customer customer = TestHelpers.generateCustomer();

        when(customerRepo.getCustomer(customer.getId()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(customer)));

        Optional<String> result = customerService.getCustomerAddress(customer.getId()).join();

        assertTrue(result.isPresent());
        assertEquals(customer.getAddress(), result.get());
    }

    @Test
    void getCustomerPhoneNumber_shouldExtractThePhoneNumberFromTheCustomer() {
        Customer customer = TestHelpers.generateCustomer();

        when(customerRepo.getCustomer(customer.getId()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(customer)));

        Optional<String> result = customerService.getCustomerPhoneNumber(customer.getId()).join();

        assertTrue(result.isPresent());
        assertEquals(customer.getPhoneNumber(), result.get());
    }
}

package com.rahul.futures;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CachedCustomerRepositoryTest {
    private CachedCustomerRepository repository;
    private ObjectStore objectStore;

    @BeforeEach
    void Setup() {
        objectStore = mock(ObjectStore.class);
        repository = new CachedCustomerRepository(objectStore);
    }

    @Test
    void save_shouldSaveTheCustomerInTheObjectStore() {
        Customer customer = TestHelpers.generateCustomer();

        when(objectStore.write(customer.getId(), customer)).thenReturn(true);

        repository.saveCustomer(customer).join();

        verify(objectStore).write(customer.getId(), customer);
    }

    @Test
    void save_shouldUpdateTheCache() {
        Customer customer = TestHelpers.generateCustomer();

        when(objectStore.write(customer.getId(), customer)).thenReturn(true);

        repository.saveCustomer(customer).join();
        Optional<Customer> result = repository.getCustomer(customer.getId()).join();

        verify(objectStore, times(1)).write(customer.getId(), customer);
        verify(objectStore, never()).read(customer.getId());

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
    }

    @Test
    void get_shouldQueryTheObjectStoreAndReturnACustomerIfPresent() {
        Customer customer = TestHelpers.generateCustomer();

        when(objectStore.read(customer.getId())).thenReturn(Optional.of(customer));

        Optional<Customer> result = repository.getCustomer(customer.getId()).join();

        verify(objectStore).read(customer.getId());
        assertTrue(result.isPresent());
        assertEquals(customer, result.get());

    }

    @Test
    void get_shouldQueryTheObjectStoreAndNotReturnACustomerIfNotPresent() {
        Customer customer = TestHelpers.generateCustomer();

        when(objectStore.read(customer.getId())).thenReturn(Optional.empty());

        Optional<Customer> result = repository.getCustomer(customer.getId()).join();

        verify(objectStore).read(customer.getId());
        assertFalse(result.isPresent());
    }

    @Test
    void get_shouldQueryTheObjectStoreForEachUniqueId() {
        Customer customer1 = TestHelpers.generateCustomer();
        Customer customer2 = TestHelpers.generateCustomer();

        when(objectStore.read(customer1.getId())).thenReturn(Optional.of(customer1));
        when(objectStore.read(customer2.getId())).thenReturn(Optional.of(customer2));

        Optional<Customer> result1 = repository.getCustomer(customer1.getId()).join();
        Optional<Customer> result2 = repository.getCustomer(customer2.getId()).join();

        verify(objectStore, times(1)).read(customer1.getId());
        verify(objectStore, times(1)).read(customer2.getId());
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
    }

    @AfterEach
    void tearDown() throws IOException {
        repository.close();
    }
}

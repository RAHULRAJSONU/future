package com.rahul.futures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceFunctionalTest {

    private Path tmpFolder;
    private FileBasedObjectStore objectStore;
    private CachedCustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setup() throws Exception {
        tmpFolder = Files.createTempDirectory("CustomerServiceFunctionalTest");

        objectStore = new FileBasedObjectStore(tmpFolder);
        customerRepository = new CachedCustomerRepository(objectStore);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void theService_shouldSaveAndRetrieveCustomers() {
        Customer expectedCustomer = TestHelpers.generateCustomer();

        UUID customerId = customerService.addCustomer(
            expectedCustomer.getFirstName(),
            expectedCustomer.getLastName(),
            expectedCustomer.getAddress(),
            expectedCustomer.getPhoneNumber()
        ).join();

        Optional<String> actualFirstName = customerService.getCustomerFirstName(customerId).join();
        assertTrue(actualFirstName.isPresent());
        assertEquals(expectedCustomer.getFirstName(), actualFirstName.get());

        Optional<String> actualLastName = customerService.getCustomerLastName(customerId).join();
        assertTrue(actualLastName.isPresent());
        assertEquals(expectedCustomer.getLastName(), actualLastName.get());

        Optional<String> actualAddress = customerService.getCustomerAddress(customerId).join();
        assertTrue(actualAddress.isPresent());
        assertEquals(expectedCustomer.getAddress(), actualAddress.get());

        Optional<String> actualPhoneNumber = customerService.getCustomerPhoneNumber(customerId).join();
        assertTrue(actualPhoneNumber.isPresent());
        assertEquals(expectedCustomer.getPhoneNumber(), actualPhoneNumber.get());
    }

    @AfterEach
    void teardown() throws Exception {
        Files.walk(tmpFolder).forEach(path ->
            path.toFile().deleteOnExit()
        );
        tmpFolder.toFile().deleteOnExit();
        customerRepository.close();
    }
}

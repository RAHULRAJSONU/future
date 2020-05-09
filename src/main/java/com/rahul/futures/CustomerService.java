package com.rahul.futures;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class CustomerService {

    private CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    CompletableFuture<UUID> addCustomer(String firstName, String lastName, String address, String phoneNumber) {
        UUID customerId = UUID.randomUUID();
        return customerRepository.saveCustomer(new Customer(
            customerId,
            firstName,
            lastName,
            address,
            phoneNumber
        )).thenApply((ignore->customerId));
    }

    CompletableFuture<Optional<String>> getCustomerFirstName(UUID customerId) {
        return customerRepository.getCustomer(customerId)
            .thenApply(customer->customer.map(Customer::getFirstName));
    }

    CompletableFuture<Optional<String>> getCustomerLastName(UUID customerId) {
        return customerRepository.getCustomer(customerId)
            .thenApply(customer -> customer.map(Customer::getLastName));
    }

    CompletableFuture<Optional<String>> getCustomerAddress(UUID customerId) {
        return customerRepository.getCustomer(customerId)
            .thenApply(customer -> customer.map(Customer::getAddress));
    }

    CompletableFuture<Optional<String>> getCustomerPhoneNumber(UUID customerId) {
        return customerRepository.getCustomer(customerId)
            .thenApply(customer -> customer.map(Customer::getPhoneNumber));
    }
}

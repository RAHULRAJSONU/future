package com.rahul.futures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void equals_shouldReturnTrue_ifCustomersAreEqual() {
        Customer cust1 = TestHelpers.generateCustomer();
        Customer cust2 = new Customer(cust1.getId(), cust1.getFirstName(), cust1.getLastName(), cust1.getAddress(), cust1.getPhoneNumber());

        assertEquals(cust1, cust2);
    }

    @Test
    void equals_shouldReturnFalse_ifCustomersAreNotEqual() {
        Customer cust1 = TestHelpers.generateCustomer();
        Customer cust2 = TestHelpers.generateCustomer();

        assertNotEquals(cust1, cust2);
    }

}

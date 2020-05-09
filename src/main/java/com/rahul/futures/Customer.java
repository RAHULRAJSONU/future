package com.rahul.futures;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Customer is an immutable data class that contains the details about a
 * customer.
 */
public class Customer implements Serializable {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String phoneNumber;

    UUID getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    String getAddress() {
        return address;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    Customer(UUID id,
                    String firstName,
                    String lastName,
                    String address,
                    String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(address, customer.address) &&
                Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, phoneNumber);
    }
}

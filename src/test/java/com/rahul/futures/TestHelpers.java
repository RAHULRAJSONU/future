package com.rahul.futures;

import java.util.Random;
import java.util.UUID;

public class TestHelpers {

    private static Random rnd = new Random(System.currentTimeMillis());

    static Customer generateCustomer() {
        return new Customer(
                UUID.randomUUID(),
                "First Name"+rnd.nextInt(1000),
                "Last Name"+rnd.nextInt(1000),
                "Address"+rnd.nextInt(1000),
                "Phone Number"+rnd.nextInt(1000));
    }
}

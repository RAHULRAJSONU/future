package com.rahul.futures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileBasedObjectStoreTest {
    private Path storeFolder;
    private ObjectStore objectStore;

    @BeforeAll
    void setup() throws Exception {
        storeFolder = Files.createTempDirectory("FileBasedObjectStoreTest");
        objectStore = new FileBasedObjectStore(storeFolder);
    }

    @Test
    void write_shouldWriteTheObjectToAFile() {
        Customer customer = TestHelpers.generateCustomer();

        boolean writeResult = objectStore.write(customer.getId(), customer);

        File expectedFile = new File(storeFolder.toFile(), customer.getId().toString());

        assertTrue(writeResult);
        assertTrue(expectedFile.exists());

        try(
            FileInputStream fileIn = new FileInputStream(expectedFile);
            ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            Customer actualCustomer = (Customer) in.readObject();
            assertEquals(customer, actualCustomer);
        } catch(Exception ex) {
            fail(ex);
        }
    }

    @Test
    void read_shouldReadTheObjectFromTheFile() {
        Customer customer = TestHelpers.generateCustomer();

        File expectedFile = new File(storeFolder.toFile(), customer.getId().toString());

        try(
            FileOutputStream fileOut = new FileOutputStream(expectedFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            out.writeObject(customer);
        } catch(Exception ex) {
            fail(ex);
        }

        Optional<Customer> result = objectStore.read(customer.getId()).map(obj -> (Customer) obj);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
    }

    @AfterEach
    void teardown() throws Exception {
        Files.walk(storeFolder).forEach(path ->
            path.toFile().deleteOnExit()
        );
        storeFolder.toFile().deleteOnExit();
    }
}

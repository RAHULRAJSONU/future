package com.rahul.futures;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

interface CustomerRepository {
    CompletableFuture<Void> saveCustomer(Customer customer);
    CompletableFuture<Optional<Customer>> getCustomer(UUID customerId);
}

class CachedCustomerRepository implements CustomerRepository, Closeable {

    private ObjectStore objectStore;
    private ConcurrentHashMap<UUID, Customer> cache = new ConcurrentHashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    CachedCustomerRepository(ObjectStore objectStore) {
        this.objectStore = objectStore;
    }

    @Override
    public CompletableFuture<Void> saveCustomer(Customer customer) {
        return CompletableFuture.runAsync(()->{
            lock.writeLock().lock();

            objectStore.write(customer.getId(), customer);
            cache.put(customer.getId(), customer);

            lock.writeLock().unlock();
        },executor);
    }

    @Override
    public CompletableFuture<Optional<Customer>> getCustomer(UUID customerId) {
        lock.readLock().lock();

        CompletableFuture<Optional<Customer>> result;

        if(cache.containsKey(customerId)) {
            result = CompletableFuture.completedFuture(Optional.of(cache.get(customerId)));
        } else {
            result = CompletableFuture.supplyAsync(
                ()->objectStore.read(customerId).map(obj -> (Customer) obj)
            ,executor);
        }

        lock.readLock().unlock();

        return result;
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
    }
}

package com.rahul.futures;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

interface ObjectStore {
    Optional<Object> read(UUID id);
    boolean write(UUID id, Object obj);
}

/**
 * The ObjectStore is a file based storage mechanism for Objects. It is
 * intended to provide blocking operations that simulate operations that would
 * traditionally use a database, an external HTTP endpoint, etc.
 */
class FileBasedObjectStore implements ObjectStore {

    private File storeFolder;

    // This would be more efficient if written with a lock per file.
    // However for simplicity we are going with a single lock instead.
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Construct an ObjectStore Store
     * @param storeFolder This folder will be used to store the object data.
     */
    FileBasedObjectStore(Path storeFolder) {
        this.storeFolder = storeFolder.toFile();
        assert(this.storeFolder.exists());
        assert(this.storeFolder.isDirectory());
    }

    /**
     * Reads an object from the file store.
     * @param id - The ID of the object
     * @return The object if it exists or empty if it does not.
     */
    @Override
    public Optional<Object> read(UUID id) {
        lock.readLock().lock();

        File file = new File(storeFolder, id.toString());

        Optional<Object> result;

        try (
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            result = Optional.ofNullable(in.readObject());
        } catch ( IOException | ClassNotFoundException ex) {
            result = Optional.empty();
        } finally {
            lock.readLock().unlock();
        }

        return result;
    }

    /**
     * Writes an object to a file in the file store.
     * @param id - The id of the object being written.
     * @param obj - The object to be written.
     * @return true if the write was successful, false if it failed.
     */
    @Override
    public boolean write(UUID id, Object obj) {
        lock.writeLock().lock();

        File file = new File(storeFolder, id.toString());

        boolean result;

        try (
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            out.writeObject(obj);
            result = true;
        } catch (IOException ex) {
            result = false;
        } finally {
            lock.writeLock().unlock();
        }

        return result;
    }
}

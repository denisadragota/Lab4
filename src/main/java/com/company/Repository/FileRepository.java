package com.company.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileRepository class implementing ICrudRepository
 * reading, storing, updating and saving information about the list of T objects in a file
 *
 * @param <T> a class from Model package
 * @author Denisa Dragota
 * @version 13.11.2021
 */
public abstract class FileRepository<T> implements ICrudRepository<T> {
    protected List<T> repoList;
    protected String filename;

    /**
     * reads data from the file and stores in the repo list
     *
     * @param filename is the name of the file where to read the data from
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public FileRepository(String filename) throws IOException {
        this.repoList = new ArrayList<>() {
        };
        this.filename = filename;
        this.repoList = readFromFile();
    }


    /**
     * reads serialized objects from the file
     *
     * @return the list of objects that were read
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public abstract List<T> readFromFile() throws IOException;

    /**
     * writes serialized objects in the file
     *
     * @param objects is the list of objects which has to be written to file
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    public abstract void write_date(Iterable<T> objects) throws IOException;


    /**
     * @return all entities
     */
    @Override
    public List<T> findAll() {

        return this.repoList;
    }


}

package com.company.Repository;
import java.util.List;

/**
 * InMemoryRepository class implementing ICrudRepository
 * storing and updating information about the list of T objects
 *
 * @param <T> a class from Model package
 *
 * @version
 *              30.10.2021
 * @author
 *              Denisa Dragota
 */
public abstract class InMemoryRepository<T> implements ICrudRepository<T> {
    protected List<T> repoList;

    public InMemoryRepository(List<T> repoList) {

        this.repoList = repoList;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<T> findAll() {

        return this.repoList;
    }





}

package com.company.Repository;

import com.company.Exceptions.NullException;

import java.io.IOException;

/**
 * CRUD operations repository interface
 * @version
 *              13.11.2021
 * @author
 *              Denisa Dragota
 */
public interface ICrudRepository<T> {
    /**
     * finds the entity with the give id from the repository list
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
    T findOne(Long id) throws NullException;

    /**
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * adds an entity in the repo list
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws NullException if input parameter entity obj is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    T save(T obj)throws NullException, IOException;

    /**
     * updates an entity from the repo list
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws NullException if input parameter entity obj is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    T update(T obj)throws NullException,IOException;

    /**
     * removes the entity with the specified id from the repo list
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    T delete(Long id)throws NullException, IOException;
}


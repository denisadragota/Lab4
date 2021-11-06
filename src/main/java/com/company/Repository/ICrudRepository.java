package com.company.Repository;

import com.company.Exceptions.NullException;

/**
 * CRUD operations repository interface
 * @version
 *              30.10.2021
 * @author
 *              Denisa Dragota
 */
public interface ICrudRepository<T> {
    /**
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
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @exception NullException if input parameter entity obj is NULL
     */

    T save(T obj)throws NullException;
    /**
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @exception NullException if input parameter entity obj is NULL
     */
    T update(T obj)throws NullException;
    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @exception NullException if input parameter id is NULL
     */
    T delete(Long id)throws NullException;
}


package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Student;

import java.util.List;

/**
 * StudentRepository class extending InMemoryRepository
 * storing and updating Student instances in repoList
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
public class StudentRepository extends InMemoryRepository<Student>{

    public StudentRepository(List<Student> repoList) {
        super(repoList);
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Student findOne(Long id) throws NullException {

        if (id == null) {
            throw new NullException("Null id!");
        }

        for(Student s: this.repoList)
        {
            if(s.getStudentId()==id)
                return s;
        }
        return null;
    }


    /**
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws NullException if input parameter entity obj is NULL
     * adding a Student object to the repo list
     * first checking if already exist, then adding
     */
    @Override
    public Student save(Student obj) throws NullException {

        if(obj==null)
            throw new NullException("Null object!");

        /* if object already exists in the repo */
        if (this.findOne(obj.getStudentId()) != null)
            return obj;

        /* add object */
        this.repoList.add(obj);
        return null;
    }

    /**
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws  NullException if input parameter entity obj is NULL
     * finds old instance with the same id as the new updated given object
     * removes the old instance and adds the updated one
     */
    @Override
    public Student update(Student obj)throws NullException {

        if(obj == null)
            throw new NullException("Null Object");

        /* find id of object to be updated */
        Student student = this.findOne(obj.getStudentId());

        /* if object does not exist in the repo*/
        if (student == null)
            return obj;

        /* update by: removing old instance and adding new given updated instance */
        this.repoList.remove(student);
        this.repoList.add(obj);
        return null;
    }

    /**
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     * deletes object with given id from the repo list
     * first checks if id exists in the repoList, then delete
     */
    @Override
    public Student delete(Long id) throws NullException{

        if(id == null)
            throw new NullException("Null id");

        /* if object does not exist in the repo */
        if (this.findOne(id) == null)
            return null;

        /*removing object with the given id */
        Student toDelete=this.findOne(id);
        this.repoList.remove(toDelete);
        return toDelete;
    }
}

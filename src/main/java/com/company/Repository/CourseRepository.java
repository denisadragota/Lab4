package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Course;

import java.util.List;

/**
 * CourseRepository class
 * storing and updating Courses instances in repoList
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
public class CourseRepository extends InMemoryRepository<Course>{
    public CourseRepository(List<Course> repoList) {

        super(repoList);
    }

    /**
     * desc: finds a Course object in the list by the id
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Course findOne(Long id) throws NullException {

        if(id == null)
            throw new NullException("Null id!");

        for(Course c: this.repoList)
        {
            if(c.getCourseId()==id)
                return c;
        }
        return null;
    }

    /**
     * adding a Course object to the repo list
     * first checking if already exist, then adding
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws NullException if input parameter entity obj is NULL
     */
    @Override
    public Course save(Course obj)throws NullException {

        if(obj == null)
            throw new NullException("Null object!");

        /* if object already exists in the repo */
        if (this.findOne(obj.getCourseId()) != null)
            return obj;

        /* add object */
        this.repoList.add(obj);
        return null;
    }

    /**
     * desc: finds old instance with the same id as the new updated given object
     * removes the old instance and adds the updated one
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws NullException if input parameter entity obj is NULL
     */
    @Override
    public Course update(Course obj) throws NullException {

        if(obj == null)
            throw new NullException("Null object!");
        /* find id of object to be updated */
        Course course = this.findOne(obj.getCourseId());

        /* if object does not exist in the repo*/
        if (course == null)
            return obj;

        /* update by: removing old instance and adding new given updated instance */
        this.repoList.remove(course);
        this.repoList.add(obj);
        return null;
    }

    /**
     * desc: deletes object with given id from the repo list
     * first checks if id exists in the repoList, then delete
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Course delete(Long id) throws NullException{

        if(id == null)
            throw new NullException("Null id!");

        /* if object does not exist in the repo */
        if (this.findOne(id) == null)
            return null;

        /*removing object with the given id */
        Course toDelete=this.findOne(id);
        this.repoList.remove(toDelete);
        return toDelete;
    }

}

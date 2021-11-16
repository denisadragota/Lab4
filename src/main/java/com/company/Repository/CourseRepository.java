package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Teacher;

import java.io.*;
import java.util.ArrayList;

/**
 * CourseRepository class extending FileRepository
 * reading, storing, updating and saving Courses instances in repoList and file
 *
 * @author Denisa Dragota
 * @version 13.11.2021
 */
public class CourseRepository extends FileRepository<Course> {

    /**
     * @param filename is the name of the file where to read the data from
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public CourseRepository(String filename) throws IOException {

        super(filename);
    }

    /**
     * if the file does not exist, instances are created and serialized written to the file,
     * and if the file already exists, than data is being read and saved in a list of courses
     *
     * @return the course List
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public ArrayList<Course> readFromFile() throws IOException {

        ArrayList<Course> courses = new ArrayList<>();
        File file = new File(this.filename);

        //file does not exist, we create instances and write them serialized to the file
        if (!file.exists()) {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.filename));
            Teacher teacher1 = new Teacher(1, "Catalin", "Rusu");
            Teacher teacher2 = new Teacher(2, "Diana", "Cristea");
            Teacher teacher3 = new Teacher(3, "Christian", "Sacarea");


            Course course1 = new Course(1, "OOP", teacher1, 20, 5);
            Course course2 = new Course(2, "SDA", teacher2, 30, 5);
            Course course3 = new Course(3, "MAP", teacher1, 3, 20);
            Course course4 = new Course(4, "NewOptional", teacher2, 3, 20);
            Course course5 = new Course(5, "Logik", teacher3, 10, 7);

            courses.add(course1);
            courses.add(course2);
            courses.add(course3);
            courses.add(course4);

            out.writeObject(course1);
            out.writeObject(course2);
            out.writeObject(course3);
            out.writeObject(course4);

            out.close();

        }
        //file already exists, data is being read and saved in a list of courses
        else {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.filename));

            try {
                Course new_course = null;
                while ((new_course = (Course) in.readObject()) != null) {
                    courses.add(new_course);
                }
            } catch (EOFException | ClassNotFoundException ignored) {
            }
        }
        return courses;
    }

    /**
     * the course repo list is being saved to the file
     *
     * @param courses is the list with Courses to save
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    public void write_date(Iterable<Course> courses) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.filename));
        for (int i = 0; i < this.repoList.size(); i++) {
            out.writeObject(this.repoList.get(i));
        }
        out.close();
    }

    /**
     * desc: finds a Course object in the list by the id
     *
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Course findOne(Long id) throws NullException {

        if (id == null)
            throw new NullException("Null id!");

        for (Course c : this.repoList) {
            if (c.getCourseId() == id)
                return c;
        }
        return null;
    }

    /**
     * adding a Course object to the repo list
     * first checking if already exist, then adding
     *
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws NullException if input parameter entity obj is NULL
     * @throws IOException   if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Course save(Course obj) throws NullException, IOException {

        if (obj == null)
            throw new NullException("Null object!");

        /* if object already exists in the repo */
        if (this.findOne(obj.getCourseId()) != null)
            return obj;

        /* add object */
        this.repoList.add(obj);
        //save changes to file
        this.write_date(this.repoList);

        return null;
    }

    /**
     * desc: finds old instance with the same id as the new updated given object
     * removes the old instance and adds the updated one
     *
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws NullException if input parameter entity obj is NULL
     * @throws IOException   if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Course update(Course obj) throws NullException, IOException {

        if (obj == null)
            throw new NullException("Null object!");
        /* find id of object to be updated */
        Course course = this.findOne(obj.getCourseId());

        /* if object does not exist in the repo*/
        if (course == null)
            return obj;

        /* update by: removing old instance and adding new given updated instance */

        this.repoList.remove(course);
        this.repoList.add(obj);
        //save changes to file
        this.write_date(this.repoList);

        return null;
    }

    /**
     * desc: deletes object with given id from the repo list
     * first checks if id exists in the repoList, then delete
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     * @throws IOException   if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Course delete(Long id) throws NullException, IOException {

        if (id == null)
            throw new NullException("Null id!");

        /* if object does not exist in the repo */
        if (this.findOne(id) == null)
            return null;

        /*removing object with the given id */
        Course toDelete = this.findOne(id);
        this.repoList.remove(toDelete);
        //save changes to file
        this.write_date(this.repoList);
        return toDelete;
    }

}

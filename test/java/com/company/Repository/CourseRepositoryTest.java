package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CourseRepositoryTest class
 * testing CourseRepository class
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
class CourseRepositoryTest {
    private Teacher teacher1;
    private Teacher teacher2;

    private Course course1;
    private Course course2;
    private Course course3;

    private CourseRepository course_repo;

    /**
     * create instances for testing before each test method
     */
    @BeforeEach
    void createInstances(){

        /* creating instances */
        teacher1 = new Teacher(1,"Catalin","Rusu");
        teacher2 = new Teacher(2,"Diana", "Cristea");

        course1 = new Course(1,"OOP",teacher1,20,6);
        course2 = new Course(2,"SDA",teacher2,30,5);
        course3 = new Course(3,"NewOptional",teacher2, 5,20);

        /* set a course list to the repo*/
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        course_repo=new CourseRepository(courses);
    }

    /**
     * test findAll() method
     */
    @Test
    void findAll(){
        /*creating the expected result list */
        Course[] courses= new Course[2];
        courses[0]=course1;
        courses[1]=course2;

        /* comparing the arrays */
        assertArrayEquals(courses,((Collection<?>)course_repo.findAll()).toArray());
    }

    /**
     * test findOne() method
     */
    @Test
    void findOne() throws NullException {
        /* search for existing course id */
        assertEquals(course1, course_repo.findOne(1L));

        /*search for non-existing course id */
        assertNull(course_repo.findOne(123L));
    }

    /**
     * test save() method
     */
    @Test
    void save() throws NullException{

        /* save course_repo size at the beginning */
        int sizeBefore=((Collection<?>)course_repo.findAll()).size();

        /* add an already existing instance in the repo */
        /* will not be added, size remains the same */
        assertEquals(course1, course_repo.save(course1));
        assertEquals(sizeBefore, ((Collection<?>) course_repo.findAll()).size());

        /* add a new instance to the repo */
        /* size of the repo increments */
        assertNull(course_repo.save(course3));
        assertEquals(sizeBefore + 1, ((Collection<?>) course_repo.findAll()).size());
    }

    /**
     * test update() method
     */
    @Test
    void update() throws NullException {

        /* try to update a course that does not exist in the repo */
        course3.setCredits(course3.getCredits() + 1);
        assertEquals(course3, course_repo.update(course3));

        /* modify the Credits attribute from existing course1 in repo */
        int creditsBefore= course1.getCredits();
        int creditsAfter= creditsBefore+1;
        course1.setCredits(creditsAfter);

        assertNull(course_repo.update(course1));
        assertEquals(creditsAfter, course_repo.findOne(course1.getCourseId()).getCredits());
    }

    /**
     * test delete() method
     */
    @Test
    void delete()throws NullException{

        /* try to delete a non-existing courseId in the repo */
        assertNull(course_repo.delete(course3.getCourseId()));


        /* delete a course from the repo */
       assertEquals(course1, course_repo.delete(course1.getCourseId()));

        /* the courseId does not exist in the repo anymore */
        assertNull(course_repo.findOne(course1.getCourseId()));
    }
}
package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TeacherRepositoryTest class
 * test TeacherRepository class
 *
 * @version
 *          13.11.2021
 * @author
 *          Denisa Dragota
 */
class TeacherRepositoryTest {
    private Teacher teacher1;
    private Teacher teacher2;
    private Teacher teacher3;

    private TeacherRepository teacher_repo;

    /**
     * create instances for testing before each test method
     */

    @BeforeEach
    void createInstances() throws IOException {
        /*creating instances*/
        /* the first 2 teachers exist in the file */
        teacher1 = new Teacher(1,"Catalin","Rusu");
        teacher2 = new Teacher(2,"Diana", "Cristea");
        teacher3 = new Teacher(3, "Christian", "Sacarea");

        teacher_repo=new TeacherRepository("teachers.ser");
    }

    /**
     * test findAll() method
     */
    @Test
    void findAll(){
        /*creating the expected result list */
        Teacher[] teachers = new Teacher[2];
        teachers[0]=teacher1;
        teachers[1]=teacher2;

        for(Teacher t:teachers){
            assertTrue(teacher_repo.findAll().contains(t));
        }
    }

    /**
     * test findOne() method
     */
    @Test
    void findOne() throws NullException{
        /* search for existing teacher id */
        assertEquals(teacher1, teacher_repo.findOne(1L));

        /*search for non-existing teacher id */
        assertNull(teacher_repo.findOne(123L));
    }

    /**
     * test save() method
     */
    @Test
    void save() throws NullException, IOException {
        /* save teacher_repo size at the beginning */
        int sizeBefore=((Collection<?>)teacher_repo.findAll()).size();

        /* add an already existing instance in the repo */
        /* will not be added, size remains the same */
        try {
            assertEquals(teacher1, teacher_repo.save(teacher1));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        assertEquals(sizeBefore, ((Collection<?>) teacher_repo.findAll()).size());

        /* add a new instance to the repo */
        /* size of the repo increments */
        try {
            assertNull(teacher_repo.save(teacher3));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        assertEquals(sizeBefore + 1, ((Collection<?>) teacher_repo.findAll()).size());

        //undo changes (the file was updated)
        this.teacher_repo.delete(teacher3.getTeacherId());
    }

    /**
     * test update() method
     */
    @Test
    void update() throws NullException, IOException {
        /* try to update a teacher that does not exist in the repo */
        teacher3.setLastName(teacher3.getLastName().substring(0, 3));
        assertEquals(teacher3, teacher_repo.update(teacher3));

        /* modify the Last Name attribute from existing teacher1 in repo */
        String nameBefore= teacher1.getLastName();
        String nameAfter= nameBefore.substring(0,3)+".";
        teacher1.setLastName(nameAfter);

        assertNull(teacher_repo.update(teacher1));
        assertEquals(nameAfter, teacher_repo.findOne(teacher1.getTeacherId()).getLastName());

        //undo changes (the file was updated)
        teacher1.setLastName(nameBefore);
        teacher_repo.update(teacher1);

    }

    /**
     * test delete() method
     */
    @Test
    void delete() throws NullException, IOException {
        /* try to delete a non-existing teacherId in the repo */
        assertNull(teacher_repo.delete(teacher3.getTeacherId()));


        /* delete a teacher from the repo */
        assertEquals(teacher1, teacher_repo.delete(teacher1.getTeacherId()));

        /* the teacherId does not exist in the repo anymore */
        assertNull(teacher_repo.findOne(teacher1.getTeacherId()));

        //undo changes (the file was updated)
        this.teacher_repo.save(teacher1);
    }
}
package com.company.Controller;

import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import com.company.Repository.CourseRepository;
import com.company.Repository.StudentRepository;
import com.company.Repository.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RegistrationSystemTest class
 * test RegistrationSystem class
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
class RegistrationSystemTest {
    private Teacher teacher1;
    private Teacher teacher2;
    private Teacher teacher3;

    private Course course1;
    private Course course2;
    private Course course3;
    private Course course4;
    private Course course5;

    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;
    private Student student5;
    private Student student6;

    private RegistrationSystem regSystem;

    /**
     * create instances for testing before each test method
     */
    @BeforeEach
    void createInstances() {

        /* creating Teacher instances */
        teacher1 = new Teacher(1, "Catalin", "Rusu");
        teacher2 = new Teacher(2, "Diana", "Cristea");
        teacher3 = new Teacher(3, "Cristian", "Sacarea");

        /* creating Course instances */
        course1 = new Course(1, "OOP", teacher1, 20, 6);
        course2 = new Course(2, "SDA", teacher2, 30, 5);
        course3 = new Course(3, "NewOptional", teacher2, 3, 20);
        course4 = new Course(4, "MAP", teacher1, 20, 5);
        course5 = new Course(5, "Optional2", teacher1, 20, 5);


        /* adding courses to each teacher*/
        List<Course> coursesTeacher1=new ArrayList<Course>();
        coursesTeacher1.add(course1);
        teacher1.setCourses(coursesTeacher1);

        List<Course> coursesTeacher2=new ArrayList<Course>();
        coursesTeacher2.add(course2);
        coursesTeacher2.add(course3);
        teacher2.setCourses(coursesTeacher2);

        /* creating a teacher Repo */
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);
        TeacherRepository teacher_repo = new TeacherRepository(teachers);

        /* creating a course Repo */
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course5);
        CourseRepository course_repo = new CourseRepository(courses);

        /* creating Student instances */
        student1 = new Student(1, "Denisa", "Dragota");
        student2 = new Student(2, "Mihnea", "Aleman");
        student3 = new Student(3, "Raul", "Barbat");
        student4 = new Student(4, "Evelin", "Bohm");
        student5 = new Student(5, "Maria", "Morar");
        student6 = new Student(6, "Iarina", "Demian");

        /* creating a Student Repo */
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        StudentRepository student_repo = new StudentRepository(students);

        /* creating a RegistrationSystem instance */
        regSystem = new RegistrationSystem(student_repo, teacher_repo, course_repo);
    }

    /**
     * test sortStudents() method
     */
    @Test
    void sortStudents(){

        List<Student> sortedStudents=this.regSystem.sortStudents();
        /* building the expected result */
        List<Student> expectedStudentsList=new ArrayList<>();
        expectedStudentsList.add(student2);
        expectedStudentsList.add(student3);
        expectedStudentsList.add(student4);
        expectedStudentsList.add(student1);
        expectedStudentsList.add(student5);
        assertArrayEquals(expectedStudentsList.toArray(),sortedStudents.toArray());
    }

    /**
     * test sortCourse() method
     */
    @Test
    void sortCourses(){
        List<Course> sortedCourses=this.regSystem.sortCourses();
        /* building the expected result */
        List<Course> expectedCoursesList=new ArrayList<>();
        expectedCoursesList.add(course2);
        expectedCoursesList.add(course1);
        expectedCoursesList.add(course3);
        assertArrayEquals(expectedCoursesList.toArray(),sortedCourses.toArray());
    }

    /**
     * test filterStudents() method
     * @throws InputException if course or student params not existing in repo list
     * or if student can not enroll to that given course
     */
    @Test
    void filterStudents()throws InputException, NullException {
        /*enroll students to courses */
        regSystem.register(course2,student1);
        regSystem.register(course3,student1);
        regSystem.register(course5,student1);

        List<Student> filteredStudents=this.regSystem.filterStudents();
        /* building the expected result */
        List<Student> expectedStudentsList=new ArrayList<>();
        expectedStudentsList.add(student1);
        assertArrayEquals(expectedStudentsList.toArray(),filteredStudents.toArray());
    }

    /**
     * test filterCourses() method
     */
    @Test
    void filterCourses() {

        List<Course> filteredCourses=this.regSystem.filterCourses();
        /* building the expected result */
        List<Course> expectedCoursesList=new ArrayList<>();
        expectedCoursesList.add(course3);
        assertArrayEquals(expectedCoursesList.toArray(),filteredCourses.toArray());
    }

    /**
     * test register() method
     */
    @Test
    void register() throws InputException, NullException {
        /* 1. register a student to a non-existing course in the Course Repo */
        Assertions.assertThrows(InputException.class,()->regSystem.register(course4, student1));

        /* 2. register a non-existing student in the Student Repo to a Course */
        Assertions.assertThrows(InputException.class,()->regSystem.register(course1, student6));

        /* 3. register 3 students to a course */
        regSystem.register(course3, student1);
        regSystem.register(course3, student2);
        regSystem.register(course3, student3);
        regSystem.register(course1, student1);

        /* assert the updates of the instances of the course and student repo */
        assertEquals(3, regSystem.findOneCourse(course3.getCourseId()).getStudentsEnrolled().size());
        assertEquals(1, regSystem.findOneStudent(student2.getStudentId()).getEnrolledCourses().size());
        assertEquals(20, regSystem.findOneStudent(student2.getStudentId()).getTotalCredits());
        assertEquals(26, regSystem.findOneStudent(student1.getStudentId()).getTotalCredits());

        /* 4. trying to enroll a student to a course with no free places */
        /* course3 has 3 total places and 3 students have been already enrolled */
        Assertions.assertThrows(InputException.class,()->regSystem.register(course3, student4));

        /* trying to enroll a student to a course that exceeds his credit limit (30) */
        /* student1 is already enrolled to 2 courses with 20 + 6 credits */
        Assertions.assertThrows(InputException.class,()->regSystem.register(course2, student1));

        /* trying to enroll a already enrolled student to the same course again */
        Assertions.assertThrows(InputException.class,()->regSystem.register(course1, student1));

    }

    /**
     * test retrieveCoursesWithFreePlaces() method
     */
    @Test
    void retrieveCoursesWithFreePlaces() throws InputException, NullException {
        /*enroll students to courses */
        /* course3 will have no places free */

        regSystem.register(course3, student1);
        regSystem.register(course3, student2);
        regSystem.register(course3, student3);
        regSystem.register(course1, student1);

        /*creating the expected result list */
        Course[] freeplacesCourses = new Course[2];
        freeplacesCourses[0] = course2;
        freeplacesCourses[1] = course1;

        assertArrayEquals(freeplacesCourses, regSystem.retrieveCoursesWithFreePlaces().toArray());
    }

    /**
     * test retrieveStudentsEnrolledForACourse() method
     */
    @Test
    void retrieveStudentsEnrolledForACourse() throws InputException,NullException {
        /* register 3 students to course3 */

        regSystem.register(course3, student1);
        regSystem.register(course3, student2);
        regSystem.register(course3, student3);

        /*creating the expected result list */
        Student[] studentsEnrolled = new Student[3];
        studentsEnrolled[0] = student1;
        studentsEnrolled[1] = student2;
        studentsEnrolled[2] = student3;

        assertArrayEquals(studentsEnrolled, regSystem.retrieveStudentsEnrolledForACourse(course3).toArray());

        /* null for a non-existing course in the Course Repo */
        assertNull(regSystem.retrieveStudentsEnrolledForACourse(course4));

        /* empty list for a course with no students enrolled */
        assertEquals(new ArrayList<Student>(), regSystem.retrieveStudentsEnrolledForACourse(course2));
    }

    /**
     * test getAllCourses() method
     */
    @Test
    void getAllCourses() {
        /*creating the expected result list */
        Course[] courses = new Course[3];
        courses[0] = course1;
        courses[1] = course2;
        courses[2] = course3;

        assertArrayEquals(courses, regSystem.getAllCourses().toArray());
    }

    /**
     * test modifyCredits() method
     */
    @Test
    void modifyCredits() throws InputException,NullException{
        /* enrolling a student to a course */

        regSystem.register(course1,student1);

        assertEquals(6, student1.getTotalCredits());

        /* modifying the credits of a course */

        course1.setCredits(course1.getCredits()+2);

        /* update in the course repo and students credits */
        regSystem.modifyCredits(course1);

        /* assert update of students credits */
        assertEquals(8, student1.getTotalCredits());
    }

    /**
     * test deleteCourseFromTeacher() method
     */
    @Test
    void deleteCourseFromTeacher() throws InputException,NullException {
        /* enroll students to a course*/

        regSystem.register(course3, student1);
        regSystem.register(course3, student2);
        regSystem.register(course3, student3);

        //number of courses of the teacher before deleting
        int coursesBefore=regSystem.findOneTeacher(((Teacher)course3.getTeacher()).getTeacherId()).getCourses().size();
        assertEquals(2,coursesBefore);

        //number credits of a student enrolled before deleting
        assertEquals(20,regSystem.findOneStudent(student1.getStudentId()).getTotalCredits());

        //delete Course
        regSystem.deleteCourseFromTeacher(teacher2, course3);

        //number of courses of the teacher before deleting
        int coursesAfter=regSystem.findOneTeacher(((Teacher)course3.getTeacher()).getTeacherId()).getCourses().size();

        //assert update of number courses
        assertEquals(coursesBefore-1,coursesAfter);

        //asserting the update of credits of the student after deleting the course
        assertEquals(0,regSystem.findOneStudent(student1.getStudentId()).getTotalCredits());

    }
}
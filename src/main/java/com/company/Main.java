package com.company;

import com.company.Controller.RegistrationSystem;
import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import com.company.Repository.CourseRepository;
import com.company.Repository.StudentRepository;
import com.company.Repository.TeacherRepository;
import com.company.View.ConsoleView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws NullException, InputException, IOException,InterruptedException {
        Teacher teacher1 = new Teacher(1, "Catalin", "Rusu");
        Teacher teacher2 = new Teacher(2, "Diana", "Cristea");
        Teacher teacher3 = new Teacher(3, "Cristian", "Sacarea");

        /* creating Course instances */
        Course course1 = new Course(1, "OOP", teacher1, 20, 6);
        Course course2 = new Course(2, "SDA", teacher2, 30, 5);
        Course course3 = new Course(3, "NewOptional", teacher2, 3, 20);
        Course course4 = new Course(4, "MAP", teacher1, 20, 5);

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
        courses.add(course4);
        CourseRepository course_repo = new CourseRepository(courses);

        /* creating Student instances */
        Student student1 = new Student(1, "Denisa", "Dragota");
        Student student2 = new Student(2, "Mihnea", "Aleman");
        Student student3 = new Student(3, "Raul", "Barbat");
        Student student4 = new Student(4, "Evelin", "Bohm");
        Student student5 = new Student(5, "Maria", "Morar");
        Student student6 = new Student(6, "Iarina", "Demian");


        /* creating a Student Repo */
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        StudentRepository student_repo = new StudentRepository(students);

        /* creating a RegistrationSystem instance */
        RegistrationSystem regSystem = new RegistrationSystem(student_repo, teacher_repo, course_repo);
        //regSystem.register(course2,student1);
        //regSystem.register(course3,student1);
        //regSystem.register(course4,student1);

        //System.out.println(regSystem.sortStudents());
        //System.out.println(regSystem.filterStudents());
        //System.out.println(regSystem.filterCourses());
        ConsoleView view=new ConsoleView(regSystem);
        view.menu();
    }
}

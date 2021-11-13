package com.company;

import com.company.Controller.RegistrationSystem;
import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Repository.CourseRepository;
import com.company.Repository.StudentRepository;
import com.company.Repository.TeacherRepository;
import com.company.View.ConsoleView;

import java.io.*;

public class Main {


    public static void main(String[] args) throws NullException, InputException, IOException {


        TeacherRepository teacher_repo = new TeacherRepository("teachers.ser");

        CourseRepository course_repo = new CourseRepository("courses.ser");

        StudentRepository student_repo = new StudentRepository("students.ser");

        RegistrationSystem regSystem = new RegistrationSystem(student_repo, teacher_repo, course_repo, "enrollment.txt");

        ConsoleView view = new ConsoleView(regSystem);
        view.menu();

    }

}







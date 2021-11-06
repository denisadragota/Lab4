package com.company.View;

import com.company.Controller.RegistrationSystem;
import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * class ConsoleView uses the RegistrationSystem controller
 * provides a menu with options for multiple actions
 * provides Input validation methods
 * Menu options: Enrolling a student to a course, showing students enrolled to a course,
 * sorting students by name, filtering student by having maximum total credits number,
 * showing available courses, adding a new course, updating credits to a course,
 * deleting a course from a teacher, sorting courses by credits number,
 * filtering courses by having moren than 10 credits
 * @version
 *
 *          06.11.2021
 *  @author
 *          Denisa Dragota
 */
public class ConsoleView {
    private RegistrationSystem controller;
    private Scanner in;

    public ConsoleView(RegistrationSystem regSystem) {
        this.controller= regSystem;
        in = new Scanner(System.in);
    }


    /**
     * validates that the input is a Long number
     * asks for a new input as long as the given input is not a Long number
     * @param message shows the message when asking for Input
     * @return the Long number given by the user
     */
    public Long validateNumberInput(String message){

        long nr=0;
        boolean option;
        //repeat until input is a Long number
        do {
            option = true;
            try {
                System.out.print(message);
                nr = in.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Wrong number! Try again...");
                option = false;
                //prepares the Scanner for a new input
                in.reset();
                in.next();
            }
        }while(!option);

        return nr;
    }

    /**
     * finds the Student with the given id
     * if the input id does not belong to any Student in the Repo, the input repeats
     * @return the Student that has id the input number
     */
    public Student validateStudentInput(){

        boolean validStudentId;
        Student givenStudent = null;
        //repeats until input is a valid Student id
        do {
            validStudentId=true;
            long stud_id=this.validateNumberInput("\nChoose id of the student you want to enroll");

            try {
                givenStudent = controller.findOneStudent(stud_id);
                //no student found
                if(givenStudent==null)
                    validStudentId=false;
            } catch (NullException e) {
                System.out.println(e.getMessage());
                validStudentId=false;
            }
        }while(!validStudentId);

        return givenStudent;
    }

    /**
     * finds the Course with the given id
     * if the input id does not belong to any Course in the Repo, the input repeats
     * @return the Course that has id the input number
     */
    public Course validateCourseInput(){

        Course givenCourse=null;
        boolean validCourseId;
        //repeats until input is a validCourse id
        do {
            validCourseId=true;

            long course_id = this.validateNumberInput("\nChoose course id");

            try {
                givenCourse = controller.findOneCourse(course_id);
                //no course found
                if(givenCourse==null)
                    validCourseId=false;
            } catch (NullException e) {
                System.out.println(e.getMessage());
                validCourseId=false;
            }
        }while(!validCourseId);
        return givenCourse;
    }

    /**
     * finds the Teacher with the given id
     * if the input id does not belong to any Teacher in the Repo, the input repeats
     * @return the Teacher that has id the input number
     */
    public Teacher validateTeacherInput(){

        boolean validTeacherId;
        Teacher newCourseTeacher=null;
        //repeats until input is a validCourse id
        do {
            long teacher_id = this.validateNumberInput("\nChoose id of teacher");
            validTeacherId = true;
            try {
                newCourseTeacher = controller.findOneTeacher(teacher_id);
                //no teacher found
                if (newCourseTeacher == null)
                    validTeacherId = false;
            } catch (NullException ex) {
                System.out.println(ex.getMessage());
                validTeacherId = false;
            }
        } while (!validTeacherId);
        return newCourseTeacher;
    }


    /**
     * gets input from the user the Student id and the Course id,
     * validates the input,
     * enrolls the student to the course
     *
     */
    public void option1(){

        System.out.println();
        for(Student stud: controller.getAllStudents()){
            System.out.println(stud);
        }

        //Choosing a Student id
        Student givenStudent=this.validateStudentInput();

        System.out.println();
        for(Course course: controller.getAllCourses()){
            System.out.println(course);
        }
        //Choosing a Course id
        Course givenCourse = this.validateCourseInput();

        //register the student to the course
        try {
            controller.register(givenCourse, givenStudent);
            System.out.println("\nSuccessfully enrolled "+ givenStudent.getFirstName()+" "+givenStudent.getLastName()+ " to course: "+givenCourse.getName());

        }catch(NullException | InputException e ){
            System.out.println(e.getMessage());
        }

    }


    /**
     * gets input from the user the course id,
     * validates the input,
     * shows the students enrolled to that course
     */
    public void option2(){

        System.out.println();
        for(Course course: controller.getAllCourses()){
            System.out.println(course);
        }

        //Choosing a course id
        Course searchedCourse = this.validateCourseInput();

            try {
                //show students enrolled
                for (Student s : controller.retrieveStudentsEnrolledForACourse(searchedCourse)) {
                    System.out.println(s);
                }
                System.out.println();
            }catch(NullException | InputException e){
                System.out.println(e.getMessage());
            }

    }

    /**
     * shows the list of students sorted ascending by last name and first name
     */
    public void option3(){
        System.out.println();
        for(Student stud: controller.sortStudents()){
            System.out.println(stud);
        }

    }

    /**
     * shows the list of students filtered by having maximum total credits number
     */
    public void option4(){
        System.out.println();
        for(Student stud: controller.filterStudents()){
            System.out.println(stud);
        }
    }

    /**
     * shows the courses with free places and how many
     */
    public void option5(){
        int freePlaces;
        for (Course course: controller.retrieveCoursesWithFreePlaces()){
            freePlaces=course.getMaxEnrollment()-course.getStudentsEnrolled().size();
            System.out.println(freePlaces+" free places in: "+ course);
        }
        System.out.println();
    }

    /**
     * Adds a new course to the Course Repo,
     * the user can choose if the course's teacher is new too,
     * and adds the teacher or updates his courses list
     */
    public void option6(){

        Teacher newCourseTeacher;
        String answear;

        //asks user if the teacher is new or not
        do {
            System.out.println("Teacher of the course is new? Y/N");
            answear = in.next();
        }while(!answear.equals("Y") && !(answear.equals("N")));

        // add new teacher to the repo
        if(answear.equals("Y")){
            System.out.println("Enter First name of the teacher");
            String newTeacherFirstName= in.next();

            System.out.println("Enter Last name of the teacher");
            String newTeacherLastName= in.next();

            Long newTeacherId= ((long) controller.getAllTeachers().size())+1;
            newCourseTeacher= new Teacher(newTeacherId,newTeacherFirstName,newTeacherLastName);
        }
        //choosing existing teacher from the repo
        else {
            System.out.println();
            for (Teacher teacher : controller.getAllTeachers()) {
                System.out.println(teacher);
            }
            newCourseTeacher=this.validateTeacherInput();
        }

        System.out.println("Enter name of the course");
        String newCourseName= in.next();

        System.out.println("Enter credits number of the course");
        int newCourseCredits= in.nextInt();

        System.out.println("Enter maximum enrollment number of the course");
        int newCourseMaxEnrollment= in.nextInt();

        Long newCourseId=((long) controller.getAllCourses().size())+1;
        Course newCourse = new Course(newCourseId,newCourseName,newCourseTeacher,newCourseMaxEnrollment,newCourseCredits);
        try {
            System.out.println(newCourse);
            //adds course to the repo
            controller.addCourse(newCourse);
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * modify the credits number for a course,
     * user gives the course id and the new credits number,
     * input is validated
     */
    public void option7(){

        System.out.println();
        for(Course course: controller.getAllCourses()){
            System.out.println(course);
        }

        Course foundCourse=this.validateCourseInput();
        int new_credits=0;
        boolean okCredits=true;
        do {
            System.out.println("\nEnter the new credits number:");
            //verify input to be an int number
            try {
                new_credits = in.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Wrong input number!");
                okCredits=false;
                in.reset();
                in.next();
            }
            //credits can be a positive number only
            if(new_credits<0)
                okCredits=false;

        }while(!okCredits);

        //updates the course
        foundCourse.setCredits(new_credits);
        try {
            controller.modifyCredits(foundCourse);
        }catch (NullException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Delete a course from a given teacher,
     * User sends input the teacher id and the course id,
     * Input is validated,
     * and the course is deleted from the teacher, from the course repo and from the students
     */
    public void option8(){

        System.out.println();
        for(Teacher t: controller.getAllTeachers()){
            System.out.println(t);
        }

        //Choosing an idTeacher
        Teacher givenTeacher=this.validateTeacherInput();


        //Print all courses from teachers
        List<Course> courseList = givenTeacher.getCourses();
        for(Course c : courseList){
            System.out.println(c.getCourseId() + "  " + c.getName());
        }

        //Choose course id
        Course choosenCourse=this.validateCourseInput();

        try {
            if (controller.deleteCourseFromTeacher(givenTeacher, choosenCourse)) {
                System.out.println("Course was deleted from teacher " + givenTeacher.getFirstName());
            }
        }catch(NullException | InputException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }


    /**
     * shows the list of courses sorted ascending by credits number
     */
    public void option9(){
        System.out.println();
        for(Course course: controller.sortCourses()){
            System.out.println(course);
        }
    }

    /**
     * shows the list of courses filtered by having more than 10 credits
     */
    public void option10(){
        System.out.println();
        for(Course course: controller.filterCourses()){
            System.out.println(course);
        }
    }


    /**
     * shows the options of the menu
     */
    public void printMenu() {
        System.out.println("---------STUDENTS---------");
        System.out.println("1. Enroll a student to a course");
        System.out.println("2. Show students enrolled to a given course");
        System.out.println("3. Sort students by name");
        System.out.println("4. Filter students by maximal credits number (30)");
        System.out.println("---------COURSES---------");
        System.out.println("5. Show available courses and the number of places");
        System.out.println("6. Add a new course");
        System.out.println("7. Update a course credits number");
        System.out.println("8. Delete a certain course");
        System.out.println("9. Sort courses by number credits");
        System.out.println("10. Filter courses by > 10 credits");
        System.out.println("0. Exit.");
    }

    /**
     * The Menu gets user input the number of the option,
     * calls the proper method for the chosen option,
     * program ends when pressing 0
     */
    public void menu() {

        boolean stay = true;
        int key=0;

        while(stay){
            //validating input (has to be an int number between 0 and 10)
            boolean option;
            do {
                printMenu();
                option = true;
                try {
                    System.out.print("Enter option: ");
                    key = in.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong number! Try again...");
                    option = false;
                    in.reset();
                    in.next();
                }
            }while(!option);

            //each option has its own method
            switch (key){
                //Exit, program ends
                case 0:
                    System.out.println("Goodbye!");
                    stay = false;
                    break;

                //Enroll a student to a course
                case 1:
                    this.option1();
                    break;

                //Show students enrolled to a given course
                case 2:
                    this.option2();
                    break;

                // Sort students by name
                case 3:
                    this.option3();
                    break;

                //Filter students by maximal credits number (30)
                case 4:
                    this.option4();
                    break;

                //Show available courses and the number of places
                case 5:
                    this.option5();
                    break;

                //Add a new course
                case 6:
                    this.option6();
                    break;

                // Update a course with a new credits number
                case 7:
                   this.option7();
                    break;

                // Delete a given course
                case 8:
                    this.option8();
                    break;

                //Sort courses by number credits
                case 9:
                    this.option9();
                    break;

                //Filter courses by > 10 credits
                case 10:
                    this.option10();
                    break;
            }
        }

    }
}

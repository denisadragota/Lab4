package com.company.Controller;

import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import com.company.Repository.CourseRepository;
import com.company.Repository.StudentRepository;
import com.company.Repository.TeacherRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * RegistrationSystem class
 * storing repo lists of: students, teachers, courses, file with current enrollment
 * <p>
 * Functionalities: enrolling a student to a course, showing courses with free places,
 * showing all courses, showing all students enrolled to a course, deleting a course from a teacher,
 * updating students' total credits number after updating a course credits, sort students by name and courses by credits numbere,
 * filter students by maximum total credit number and courses with over 10 credits,
 * reading and saving enrollment to file
 *
 * @author Denisa Dragota
 * @version 13.11.2021
 */
public class RegistrationSystem {
    private StudentRepository studentsRepo;
    private TeacherRepository teachersRepo;
    private CourseRepository coursesRepo;
    private String filename;

    public RegistrationSystem(StudentRepository studentsRepo,
                              TeacherRepository teachersRepo,
                              CourseRepository coursesRepo,
                              String filename) throws NullException, InputException, IOException {
        this.studentsRepo = studentsRepo;
        this.teachersRepo = teachersRepo;
        this.coursesRepo = coursesRepo;
        this.filename = filename;
        this.readEnrollment();
    }


    /**
     * Reads from file the enrollment between students and courses, and adds the lists
     * to the Students and Courses
     *
     * @throws NullException  if idCourse or idStudent is null
     * @throws InputException if course or student parameter in register method is null
     * @throws IOException    if there occurs an error with the Scanner
     */
    public void readEnrollment() throws NullException, InputException, IOException {
        try {
            File file = new File(this.filename);
            Scanner myReader = new Scanner(file);

            while (myReader.hasNextLine()) {
                //format of lines in the file:   idStudent idCourse

                String data = myReader.nextLine();
                String[] enrolled = data.split(" ");
                Long idStudent = Long.parseLong(enrolled[0]);
                Long idCourse = Long.parseLong(enrolled[1]);
                Course course = this.coursesRepo.findOne(idCourse);
                Student student = this.studentsRepo.findOne(idStudent);

                //enroll student to course
                this.register(course, student);

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        for (Course c : this.coursesRepo.findAll()) {
            this.addCourse(c);
        }
    }

    /**
     * save to the enrollment file all changes
     *
     * @throws IOException if there occurs an error with the FileWriter
     */
    public void writeEnrollment() throws IOException {
        try {
            FileWriter myWriter = new FileWriter(this.filename);
            for (Student stud : this.getAllStudents()) {
                for (Course course : stud.getEnrolledCourses())
                    myWriter.write(Long.toString(stud.getStudentId()) + " " + Long.toString(course.getCourseId()) + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * sort the students repo list by LastName and FirstName
     *
     * @return the sorted list
     */
    public List<Student> sortStudents() {
        List<Student> sortedStudents = new ArrayList<>();
        sortedStudents = this.getAllStudents();
        sortedStudents.sort(Comparator.comparing(Student::getLastName)
                .thenComparing(Student::getFirstName));
        return sortedStudents;
    }

    /**
     * sort the course repo list by credits number ascending
     *
     * @return the sorted list
     */
    public List<Course> sortCourses() {
        List<Course> sortedCourses = new ArrayList<>();
        sortedCourses = this.getAllCourses();
        sortedCourses.sort(Comparator.comparing(Course::getCredits)
                .thenComparing(Course::getName));
        return sortedCourses;
    }

    /**
     * filter the students repo list by condition: having maximum credits number (30)
     *
     * @return the filtered list
     */
    public List<Student> filterStudents() {
        List<Student> filteredStudents = new ArrayList<>();

        for (Student stud : this.studentsRepo.findAll()) {
            if (stud.getTotalCredits() == 30) {
                filteredStudents.add(stud);
            }
        }
        return filteredStudents;
    }

    /**
     * filter the course repo list by condition: having more than 10 credits
     *
     * @return the filtered list
     */
    public List<Course> filterCourses() {
        List<Course> filteredCourses = new ArrayList<>();

        for (Course course : this.coursesRepo.findAll()) {
            if (course.getCredits() > 10) {
                filteredCourses.add(course);
            }
        }
        return filteredCourses;
    }

    /**
     * desc: enroll a student to a course
     *
     * @param course   , Course object
     * @param student, Student object
     * @return true if successfully enroled, else false
     * @throws InputException if course or student params not existing in repo lists
     *                        or if student can not enroll to that given course under the following conditions
     *                        Conditions: student can have maximal 30 credits and a course has a maximum number of enrolled students,
     *                        student can not be enrolled multiple times to the same course
     * @throws NullException  if course or student Id is null
     * @throws IOException    if there occurs an error with the FileWriter in the writeEnrollment() method
     *                        or with the ObjectOutputStream in the update() method
     */
    public boolean register(Course course, Student student) throws InputException, NullException, IOException {

        //check if course exists in repo
        if (course == null || coursesRepo.findOne(course.getCourseId()) == null) {
            throw new InputException("Non-existing course id!");
        }

        //check if student exists in repo
        if (student == null || studentsRepo.findOne(student.getStudentId()) == null) {
            throw new InputException("Non-existing student id!");
        }
        List<Student> courseStudents = course.getStudentsEnrolled();
        //check if course has free places
        if (courseStudents.size() == course.getMaxEnrollment()) {
            throw new InputException("Course has no free places!");
        }

        //check if student is already enrolled
        for (Student s : courseStudents) {
            if (s.compareTo(student))
                throw new InputException("Student already enrolled!");
        }

        //if student has over 30 credits after enrolling to this course
        int studCredits = student.getTotalCredits() + course.getCredits();
        if (studCredits > 30)
            throw new InputException("Total number of credits exceeded!");
        ;

        //add student to course
        //update courses repo
        courseStudents.add(student);
        course.setStudentsEnrolled(courseStudents);
        coursesRepo.update(course);

        //update total credits of student
        student.setTotalCredits(studCredits);

        //update enrolled courses of Student
        List<Course> studCourses = student.getEnrolledCourses();
        studCourses.add(course);
        student.setEnrolledCourses(studCourses);

        //update students Repo
        studentsRepo.update(student);

        //update in the enrollment file
        this.writeEnrollment();

        return true;
    }

    /**
     * desc: find courses from the course repo where number of enrolled students is less than maximum enroll limit
     *
     * @return courses with free places
     */
    public List<Course> retrieveCoursesWithFreePlaces() {
        List<Course> freePlaces = new ArrayList<>();

        for (Course c : coursesRepo.findAll()) {
            /* comparing number of enrolled students with the limit */
            if (c.getStudentsEnrolled().size() < c.getMaxEnrollment())
                freePlaces.add(c);
        }
        return freePlaces;
    }

    /**
     * desc: retrieve all students enrolled to a course
     *
     * @param course Course object
     * @return list of students enrolled to the given course, or null if course is NULL
     * @throws InputException if the course is null
     * @throws NullException  if the courseId is null
     */
    public List<Student> retrieveStudentsEnrolledForACourse(Course course) throws InputException, NullException {
        if (course == null) {
            throw new InputException("Non-existing course id!");
        }
        if (coursesRepo.findOne(course.getCourseId()) != null) {
            return course.getStudentsEnrolled();
        }

        return null;
    }

    /**
     * desc: Delete a course from a teacher. Removing course from the teacher's courses list, from the students' enrolled lists and from the course repo
     *
     * @param teacher Teacher object from whom we delete a course
     * @param course  Course object, from the teacher's list, to be deleted
     * @return true if successfully deleted
     * @throws InputException if teacher or course do not exist in te repo lists,
     *                        or if the course does not correspond to that teacher
     *                        deleting course from the teacher's teaching list, from the students enrolled list and from the courses repo
     * @throws NullException  if course or id  is null
     * @throws IOException    if there occurs an error with the ObjectOutputStream in the update() or remove() method
     */
    public boolean deleteCourseFromTeacher(Teacher teacher, Course course) throws InputException, NullException, IOException {

        //check if course exists
        if (coursesRepo.findOne(course.getCourseId()) == null) {
            throw new InputException("Non-existing course id!");
        }

        //check if teacher exists
        if (teachersRepo.findOne(teacher.getTeacherId()) == null) {
            throw new InputException("Non-existing teacher id!");
        }

        //check if course actually is in the teacher's list of courses
        List<Course> courseList = teacher.getCourses();
        Optional<Course> c = courseList.stream().filter(el -> el.compareTo(course)).findFirst();

        // course not found in teacher courses list
        if (c.isEmpty())
            throw new InputException("Course id not corresponding to teacher id!");
        else {   //delete course from teacher's list
            List<Student> studentsEnrolled = course.getStudentsEnrolled(); //store students enrolled
            courseList.remove(c.get());
            teacher.setCourses(courseList);
            teachersRepo.update(teacher);

            //delete course from Course Repo
            coursesRepo.delete(course.getCourseId());

            //delete course from all students enrolled
            for (Student s : studentsEnrolled) {

                List<Course> coursesEnrolled = s.getEnrolledCourses();
                coursesEnrolled.remove(course);
                //update student with the new courses enrolled list and the new credits
                s.setEnrolledCourses(coursesEnrolled);
                s.setTotalCredits(s.getTotalCredits() - course.getCredits());

                //update in the Repo
                this.studentsRepo.update(s);
            }
            return true;
        }
    }

    /**
     * Saves to all files, all changes (Students file, Teachers file, Courses file, Enrollment file)
     *
     * @throws IOException if there occurs an error with the FileWriter or with the ObjectOutputStream
     */
    public void saveExit() throws IOException {
        this.writeEnrollment();
        this.teachersRepo.write_date(this.getAllTeachers());
        this.studentsRepo.write_date(this.getAllStudents());
        this.coursesRepo.write_date(this.getAllCourses());

    }

    /**
     * desc: Recalculate the sum of credits provided from the enrolled courses of the students
     * Update the credits sum for each student
     *
     * @throws NullException if id of a student is null
     * @throws IOException   if there occurs an error with the ObjectOutputStream in the update() method
     */
    public void updateStudentsCredits() throws NullException, IOException {
        List<Student> stud = this.getAllStudents();

        for (Student s : stud) {
            List<Course> coursesEnrolled = s.getEnrolledCourses();
            int sum = 0;
            //looping through all enrolled courses of each student
            for (Course c : coursesEnrolled) {
                //calculate the total sum of credits
                sum += c.getCredits();
            }

            //update the total sum of credits for the student
            s.setTotalCredits(sum);

            //update in the repo
            studentsRepo.update(s);
        }
    }

    /**
     * desc: modifying credit number for a course, that leads to updating repo with the updated course and updating students' credits
     *
     * @param c Course object, which credits were updated
     * @throws NullException if id of a course is null
     * @throws IOException   if there occurs an error with the ObjectOutputStream in the update() method
     */
    public void modifyCredits(Course c) throws NullException, IOException {
        /* update course in the repo */
        this.coursesRepo.update(c);

        /*update all students*/
        this.updateStudentsCredits();
    }

    /**
     * save course to the Course repo,
     * save the Teacher to the Teacher repo if he is new
     * and updates the teacher's course list
     *
     * @param c course to be added
     * @throws NullException if course id is null
     * @throws IOException   if there occurs an error with the ObjectOutputStream in the save() or update() method
     */
    public boolean addCourse(Course c) throws NullException, IOException {
        //saving course to the Course repo
        try {
            this.coursesRepo.save(c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //updating the course list of the Teacher
        Teacher newTeacher = (Teacher) c.getTeacher();
        List<Course> updatedCourses = newTeacher.getCourses();

        //check if the teacher already has the course in his list
        for (Course course : updatedCourses)
            if (course.getCourseId() == c.getCourseId())
                return false;
        updatedCourses.add(c);
        newTeacher.setCourses(updatedCourses);
        //adding the teacher to the Teacher repo, if he is new
        if (this.teachersRepo.findOne(newTeacher.getTeacherId()) == null) {
            try {
                this.teachersRepo.save(newTeacher);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else //updating the course list of the teacher
        {
            this.teachersRepo.update(newTeacher);
        }
        return true;
    }


    /**
     * desc: get all students from the repo
     *
     * @return student list from the student repo
     */
    public List<Student> getAllStudents() {
        ArrayList<Student> allStudents = new ArrayList<>();
        for (Student s : this.studentsRepo.findAll()) {
            allStudents.add(s);
        }
        return allStudents;
    }

    /**
     * desc: get all courses from the repo
     *
     * @return courses list from the course repo
     */
    public List<Course> getAllCourses() {
        ArrayList<Course> allCourses = new ArrayList<>();
        for (Course c : this.coursesRepo.findAll()) {
            allCourses.add(c);
        }
        return allCourses;
    }

    /**
     * get all teachers from the repo
     *
     * @return teachers list from teh teacher repo
     */
    public List<Teacher> getAllTeachers() {
        ArrayList<Teacher> allTeachers = new ArrayList<>();
        for (Teacher t : this.teachersRepo.findAll()) {
            allTeachers.add(t);
        }
        return allTeachers;
    }

    /**
     * searching for a student in the repo by the id
     *
     * @param id of a Student object
     * @return Student object from the student repo list with the given id
     * @throws NullException if student id is null
     */
    public Student findOneStudent(long id) throws NullException {
        return this.studentsRepo.findOne(id);
    }

    /**
     * desc: searching for a course in the repo by the id
     *
     * @param id of a Course object
     * @return Course object from the course repo list with the given id
     * @throws NullException if course id is null
     */
    public Course findOneCourse(long id) throws NullException {
        return this.coursesRepo.findOne(id);
    }

    /**
     * desc: searching for a teacher in the repo by the id
     *
     * @param id of a Teacher object
     * @return Teacher object from the teacher repo list with the given id
     * @throws NullException if teacher id is null
     */
    public Teacher findOneTeacher(long id) throws NullException {
        return this.teachersRepo.findOne(id);
    }


}

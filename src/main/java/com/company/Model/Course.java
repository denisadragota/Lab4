package com.company.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * class Course
 * stores and provides information about a course's: name, id, teacher, maximal enrollment number,
 * enrolled students, number of credits
 *
 * @version
 *          30.10.2021
 *
 * @author
 *          Denisa Dragota
 */
public class Course {
    private String name;
    private long courseId; //unique identifier of an object
    private Person teacher;
    private int maxEnrollment;
    private List<Student> studentsEnrolled;
    private int credits;

    public Course(long courseId,String name, Person teacher, int maxEnrollment,  int credits) {
        this.courseId=courseId;
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled = new ArrayList<>();
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Student> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Student> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }


    /**
     * comparation based on id (unique identifier)
     * @param other, a Course object
     * @return true if objects are equal, else false
     */
    public boolean compareTo(Course other) {
        /* comparing based on id */
        if(this.courseId ==  other.getCourseId())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", courseId=" + courseId +
                ", teacher=" + teacher +
                ", maxEnrollment=" + maxEnrollment +
                ", credits=" + credits +
                '}';
    }
}

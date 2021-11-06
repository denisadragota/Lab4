package com.company.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * class Teacher extends abstract class Person
 * stores and provides information about a teacher's: id, first name, last name, list of courses
 *
 * @version
 *
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
public class Teacher extends Person{
    private long teacherId; //unique identifier of an object
    private List<Course> courses;

    public Teacher(long teacherId, String firstName, String lastName) {
        this.teacherId=teacherId;
        this.courses = new ArrayList<>();
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", teacherId=" + teacherId +
                '}';
    }

    /**
     * comparation based on id (unique identifier)
     * @param other, Teacher object to compare with
     * @return true if objects are equal, else false
     *
     */
    public boolean compareTo(Teacher other) {
        /* comparing id */
        if(this.teacherId ==  other.getTeacherId())
            return true;
        return false;
    }
}

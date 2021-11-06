package com.company.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class Student extends abstract class Person
 * stores and provides information about a student's : id, first name, last name,
 * total credits number and enrolled courses
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
public class Student extends Person{
    private long studentId; //unique identifier of an object
    private int totalCredits;
    private List<Course> enrolledCourses;

    public Student(long studentId,String firstName, String lastName) {
        this.studentId = studentId;
        this.totalCredits = 0;
        this.enrolledCourses = new ArrayList<>(){};
        this.firstName=firstName;
        this.lastName=lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId && totalCredits == student.totalCredits && Objects.equals(enrolledCourses, student.enrolledCourses);
    }


    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * comparation based on id (unique identifier)
     * @param other, Student object to compare with
     * @return true if objects are equal, else false
     *
     */
    public boolean compareTo(Student other) {

        /* comparing id */
        if(this.studentId ==  other.getStudentId())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studentId=" + studentId +
                ", totalCredits=" + totalCredits +
                '}';
    }
}

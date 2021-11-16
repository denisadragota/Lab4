package com.company.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class Student extends abstract class Person
 * stores and provides information about a student's : id, first name, last name,
 * total credits number and enrolled courses
 *
 * @author Denisa Dragota
 * @version 13.11.2021
 */
public class Student extends Person implements Serializable {
    private long studentId; //unique identifier of an object
    private transient int totalCredits;
    @Serial
    private static final long serialVersionUID = 1L;
    private transient List<Course> enrolledCourses;

    public Student(long studentId, String firstName, String lastName) {
        this.studentId = studentId;
        this.totalCredits = 0;
        this.enrolledCourses = new ArrayList<>() {
        };
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student() {
    }

    ;

    /**
     * writes serialized Student objects to the file
     * custom serialization for the attribute (courses enrolled list) as an empty list
     * in order to avoid circular references
     *
     * @param oos is the ObjectOutputStream object
     * @throws IOException if there occurs an error with the ObjectOutputStream object
     */
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>() {
        });
    }

    /**
     * reads serialized objects from the file,
     * reads the custom serialized transient attribute (enrolled courses list)
     *
     * @param ois is an ObjectInputStream object
     * @throws ClassNotFoundException
     * @throws IOException            if there occurs an error with the ObjectInputStream
     */
    @Serial
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.enrolledCourses = (List<Course>) ois.readObject();
        this.totalCredits = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId;
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
     *
     * @param other, Student object to compare with
     * @return true if objects are equal, else false
     */
    public boolean compareTo(Student other) {

        /* comparing id */
        return this.studentId == other.getStudentId();
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

package com.company.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class Teacher extends abstract class Person
 * stores and provides information about a teacher's: id, first name, last name, list of courses
 *
 * @author Denisa Dragota
 * @version 13.11.2021
 */
public class Teacher extends Person implements Serializable {
    private long teacherId; //unique identifier of an object
    @Serial
    private static final long serialVersionUID = 1L;
    private transient List<Course> courses;

    public Teacher(long teacherId, String firstName, String lastName) {
        this.teacherId = teacherId;
        this.courses = new ArrayList<>() {
        };
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Teacher() {
    }

    ;


    /**
     * writes serialized Student objects to the file
     * custom serialization for the attribute (teaching courses list) as an empty list
     * in order to avoid circular references
     *
     * @param oos is the ObjectOutputStream object
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>() {
        });
    }

    /**
     * reads serialized objects from the file,
     * reads the custom serialized transient attribute (teaching courses list)
     *
     * @param ois is an ObjectInputStream object
     * @throws ClassNotFoundException
     * @throws IOException            if there occurs an error with the ObjectInputStream
     */
    @Serial
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.courses = (List<Course>) ois.readObject();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return teacherId == teacher.teacherId;
    }


}

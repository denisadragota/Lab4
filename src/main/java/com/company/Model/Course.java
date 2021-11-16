package com.company.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class Course
 * stores and provides information about a course's: name, id, teacher, maximal enrollment number,
 * enrolled students, number of credits
 *
 * @author Denisa Dragota
 * @version 13.11.2021
 */
public class Course implements Serializable {
    private String name;
    private long courseId; //unique identifier of an object
    private Teacher teacher;
    private int maxEnrollment;
    @Serial
    private static final long serialVersionUID = 1L;
    private transient List<Student> studentsEnrolled;
    private int credits;

    public Course(long courseId, String name, Teacher teacher, int maxEnrollment, int credits) {
        this.courseId = courseId;
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled = new ArrayList<>() {
        };
        this.credits = credits;
    }

    public Course() {
    }

    ;

    /**
     * writes serialized Course objects to the file
     * custom serialization for the attribute (students enrolled list) as an empty list,
     * in order to avoid circular references
     *
     * @param oos the ObjectOutputStream object
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
     * reads the custom serialized transient attribute (enrolled students list)
     *
     * @param ois is the ObjectInputStream object
     * @throws ClassNotFoundException
     * @throws IOException            if there occurs an error with the ObjectInputStream
     */
    @Serial
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.studentsEnrolled = (List<Student>) ois.readObject();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
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
     *
     * @param other, a Course object
     * @return true if objects are equal, else false
     */
    public boolean compareTo(Course other) {
        /* comparing based on id */
        return this.courseId == other.getCourseId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId == course.courseId;
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

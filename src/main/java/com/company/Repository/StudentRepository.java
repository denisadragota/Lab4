package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Student;

import java.io.*;
import java.util.ArrayList;

/**
 * StudentRepository class extending FileRepository
 * reading, storing, updating and saving Student instances in repoList and file
 *
 * @version
 *          13.11.2021
 * @author
 *          Denisa Dragota
 */
public class StudentRepository extends FileRepository<Student>{

    /**
     * @param filename is the name of the file where to read the data from
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public StudentRepository(String filename) throws IOException{

        super(filename);
    }

    /**
     * if the file does not exist, instances are created and serialized written to the file,
     * and if the file already exists, than data is being read and saved in a list of students
     * @param filename is the filename where to read data from
     * @return the students list
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public  ArrayList<Student> readFromFile(String filename) throws IOException{

        ArrayList<Student> students=new ArrayList<>();
        File file = new File (this.filename);

        //file does not exist, we create instances and write them serialized to the file
        if (!file.exists()) {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.filename));
            Student student1 = new Student(1, "Denisa", "Dragota");
            Student student2 = new Student(2, "Mihnea", "Aleman");
            Student student3 = new Student(3, "Raul", "Barbat");
            Student student4 = new Student(4, "Evelin", "Bohm");


            students.add(student1);
            students.add(student2);
            students.add(student3);
            students.add(student4);

            out.writeObject(student1);
            out.writeObject(student2);
            out.writeObject(student3);
            out.writeObject(student4);

            out.close();

        }else //file already exists, data is being read and saved in a list of students
        {

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.filename));

        try {
            Student new_student= null;
            while ((new_student = (Student) in.readObject()) != null) {
                students.add(new_student);
            }
        }catch(EOFException | ClassNotFoundException exc){
            System.out.println("end of file");
        }
        }
        return students;

    }

    /**
     * the students repo list is being saved to the file
     * @param students is the list with Students to save
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    public void write_date(Iterable<Student> students) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.filename));
        for(int i=0;i<this.repoList.size();i++){
            out.writeObject(this.repoList.get(i));
        }

        out.close();
    }


    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Student findOne(Long id) throws NullException {

        if (id == null) {
            throw new NullException("Null id!");
        }

        for(Student s: this.repoList)
        {
            if(s.getStudentId()==id)
                return s;
        }
        return null;
    }


    /**
     * adding a Student object to the repo list
     * first checking if already exist, then adding
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws NullException if input parameter entity obj is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Student save(Student obj) throws NullException, IOException {

        if(obj==null)
            throw new NullException("Null object!");

        /* if object already exists in the repo */
        if (this.findOne(obj.getStudentId()) != null)
            return obj;

        /* add object */
        this.repoList.add(obj);
        //save changes to file
        this.write_date(this.repoList);
        return null;
    }

    /**
     * finds old instance with the same id as the new updated given object
     * removes the old instance and adds the updated one
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws  NullException if input parameter entity obj is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Student update(Student obj)throws NullException,IOException {

        if(obj == null)
            throw new NullException("Null Object");

        /* find id of object to be updated */
        Student student = this.findOne(obj.getStudentId());

        /* if object does not exist in the repo*/
        if (student == null)
            return obj;

        /* update by: removing old instance and adding new given updated instance */
        this.repoList.remove(student);
        this.repoList.add(obj);
        //save changes to file
        this.write_date(this.repoList);
        return null;
    }

    /**
     * deletes object with given id from the repo list
     * first checks if id exists in the repoList, then delete
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Student delete(Long id) throws NullException, IOException{

        if(id == null)
            throw new NullException("Null id");

        /* if object does not exist in the repo */
        if (this.findOne(id) == null)
            return null;

        /*removing object with the given id */
        Student toDelete=this.findOne(id);
        this.repoList.remove(toDelete);
        //save changes to file
        this.write_date(this.repoList);
        return toDelete;
    }
}

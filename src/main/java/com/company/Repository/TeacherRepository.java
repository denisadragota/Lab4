package com.company.Repository;


import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Teacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TeacherRepository class extending FileRepository
 * reading, storing, updating and saving Teacher instances in repoList and file
 *
 * @version
 *          13.11.2021
 * @author
 *          Denisa Dragota
 */
public class TeacherRepository extends FileRepository<Teacher>{
    /**
     * @param filename is the name of the file where to read the data from
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public TeacherRepository(String filename) throws IOException{

        super(filename);
    }

    /**
     * if the file does not exist, instances are created and serialized written to the file,
     * and if the file already exists, than data is being read and saved in a list of teachers
     * @param filename is the filename where to read data from
     * @return the teachers list
     * @throws IOException if there occurs an error with the ObjectInputStream
     */
    public ArrayList<Teacher> readFromFile(String filename) throws IOException {

        ArrayList<Teacher> teachers = new ArrayList<>();
        File file = new File (this.filename);

        //file does not exist, we create instances and write them serialized to the file
        if (!file.exists()) {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.filename));
            Teacher teacher1 = new Teacher(1, "Catalin", "Rusu");
            Teacher teacher2 = new Teacher(2, "Diana", "Cristea");

            teachers.add(teacher1);
            teachers.add(teacher2);

            out.writeObject(teacher1);
            out.writeObject(teacher2);

            out.close();

        }
        else //file already exists, data is being read and saved in a list of teachers
        {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.filename));
        try {
            Teacher new_teacher= null;
            while ((new_teacher = (Teacher) in.readObject()) != null) {
                teachers.add(new_teacher);
            }
        }catch(EOFException | ClassNotFoundException exc){
            System.out.println("end of file");
        }
        }
        return teachers;
    }


    /**
     * the teachers repo list is being saved to the file
     * @param teachers is the list with teachers to save
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    public void write_date(Iterable<Teacher> teachers) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.filename));
        for(int i=0;i<this.repoList.size();i++){
            out.writeObject(this.repoList.get(i));
        }

        out.close();
    }

    /**
     * desc: finds a teacher object by id
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Teacher findOne(Long id) throws NullException {

        if(id==null)
            throw new NullException("Null id!");

        for(Teacher t: this.repoList)
        {
            if(t.getTeacherId()==id)
                return t;
        }
        return null;
    }

    /**
     * desc: adding a Teacher object to the repo list
     * first checking if already exist, then adding
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws NullException if input parameter entity obj is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Teacher save(Teacher obj) throws NullException, IOException {

        if(obj==null)
            throw new NullException("Null obj!");

        /* if object already exists in the repo */
        if (this.findOne(obj.getTeacherId()) != null)
            return obj;

        /* add object */
        this.repoList.add(obj);

        //save changes to file
        this.write_date(this.repoList);
        return null;
    }

    /**
     * desc: finds old instance with the same id as the new updated given object
     * removes the old instance and adds the updated one
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws NullException if input parameter entity obj is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Teacher update(Teacher obj) throws NullException, IOException {

        if(obj==null)
            throw new NullException("Null object!");

        /* find id of object to be updated */
        Teacher teacher = this.findOne(obj.getTeacherId());

        /* if object does not exist in the repo*/
        if (teacher == null)
            return obj;

        /* update by: removing old instance and adding new given updated instance */
        this.repoList.remove(teacher);
        this.repoList.add(obj);

        //save changes to file
        this.write_date(this.repoList);
        return null;
    }

    /**
     * desc: deletes object with given id from the repo list
     * first checks if id exists in the repoList, then delete
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     * @throws IOException if there occurs an error with the ObjectOutputStream
     */
    @Override
    public Teacher delete(Long id) throws NullException, IOException {

        if(id==null)
            throw new NullException("Null id!");

        /* if object does not exist in the repo */
        if (this.findOne(id) == null)
            return null;

        /*removing object with the given id */
        Teacher toDelete=this.findOne(id);
        this.repoList.remove(toDelete);

        //save changes to file
        this.write_date(this.repoList);
        return toDelete;
    }

}

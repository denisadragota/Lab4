package com.company.Model;

/**
 * Abstract class Person
 * stores and provides information about a person's: first and last name
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
public abstract class Person {
    protected String firstName;
    protected String lastName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}


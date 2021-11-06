package com.company.Exceptions;

/**
 * NullException class extends Exception
 * used for the Repository classes, preventing having null as a parameter in the methods
 *
 * @version
 *
 *          30.10.2021
 * @author
 *
 *          Denisa Dragota
 */
public class NullException extends Exception{
    public NullException(String s){
        super(s);
    }
}

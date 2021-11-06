package com.company.Exceptions;

/**
 * InputException class extends Exception
 * used for wrong input parameters in the RegistrationSystem (Controller) class
 * wrong input meaning: non-existing instances in the repositories or impossible instances to perform an action
 *
 * @version
 *             30.10.2021
 *
 * @author
 *             Denisa Dragota
 */
public class InputException extends Exception{
    public InputException(String s){
        super(s);
    }
}

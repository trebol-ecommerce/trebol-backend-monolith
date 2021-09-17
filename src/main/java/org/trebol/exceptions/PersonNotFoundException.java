package org.trebol.exceptions;

import javassist.NotFoundException;

public class PersonNotFoundException extends NotFoundException {

    public PersonNotFoundException(String msg) {
        super(msg);
    }

    public PersonNotFoundException(String msg, Exception e) {
        super(msg, e);
    }
}

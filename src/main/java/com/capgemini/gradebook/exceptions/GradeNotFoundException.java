package com.capgemini.gradebook.exceptions;

public class GradeNotFoundException extends RuntimeException {

    public GradeNotFoundException(String message) {

        super(message);
    }
}
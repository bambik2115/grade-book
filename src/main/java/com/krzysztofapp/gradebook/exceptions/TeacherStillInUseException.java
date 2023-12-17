package com.krzysztofapp.gradebook.exceptions;

public class TeacherStillInUseException extends RuntimeException {

    public TeacherStillInUseException(String message) {

        super(message);
    }
}

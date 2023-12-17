package com.krzysztofapp.gradebook.exceptions;

public class GradeAlreadyCreatedTodayException extends RuntimeException {

    public GradeAlreadyCreatedTodayException(String message) {

        super(message);
    }
}
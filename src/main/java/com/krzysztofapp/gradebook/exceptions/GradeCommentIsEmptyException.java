package com.krzysztofapp.gradebook.exceptions;

public class GradeCommentIsEmptyException extends RuntimeException {

    public GradeCommentIsEmptyException(String message) {

        super(message);
    }
}
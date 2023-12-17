package com.krzysztofapp.gradebook.domain;

import com.krzysztofapp.gradebook.persistence.entity.GradeType;

import java.time.LocalDate;

public class GradeContext {

    LocalDate dateOfGrade;

    GradeType gradeType;

    public LocalDate getDateOfGrade() {
        return this.dateOfGrade;
    }

    public void setDateOfGrade(LocalDate dateOfGrade) {
        this.dateOfGrade = dateOfGrade;
    }

    public GradeType getGradeType() {
        return this.gradeType;
    }

    public void setGradeType(GradeType gradeType) {
        this.gradeType = gradeType;
    }
}

package com.capgemini.gradebook.domain;

import javax.validation.constraints.NotNull;

public class ClassYearEto extends AbstractEto {

    @NotNull
    private Integer classLevel;

    @NotNull
    private String className;

    @NotNull
    private String classYear;


    public Integer getClassLevel() {
        return this.classLevel;
    }

    public void setClassLevel(Integer classLevel) {
        this.classLevel = classLevel;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassYear() {
        return this.classYear;
    }

    public void setClassYear(String classYear) {
        this.classYear = classYear;
    }
}

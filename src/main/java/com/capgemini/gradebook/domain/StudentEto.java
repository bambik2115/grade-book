package com.capgemini.gradebook.domain;

import javax.validation.constraints.NotNull;

public class StudentEto extends AbstractEto {

    @NotNull
    private Long classYearId;

    private Integer age;

    private String firstName;

    private String lastName;


    public Long getClassYearId() {
        return this.classYearId;
    }

    public void setClassYearId(Long classYearId) {
        this.classYearId = classYearId;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

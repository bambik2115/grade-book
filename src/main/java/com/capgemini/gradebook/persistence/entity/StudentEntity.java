package com.capgemini.gradebook.persistence.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STUDENT")
public class StudentEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassYear classYear;

    private Integer age;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "studentEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Grade> studentGradeList;

    public ClassYear getClassYear() {
        return this.classYear;
    }

    public void setClassYear(ClassYear classYear) {
        this.classYear = classYear;
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

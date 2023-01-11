package com.capgemini.gradebook.persistence.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STUDENT")
public class StudentEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassYearEntity classYearEntity;

    private Integer age;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "studentEntity", cascade = CascadeType.REMOVE)
    private List<GradeEntity> gradeList;

    public ClassYearEntity getClassYearEntity() {
        return this.classYearEntity;
    }

    public void setClassYearEntity(ClassYearEntity classYearEntity) {
        this.classYearEntity = classYearEntity;
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

    public List<GradeEntity> getGradeList() {
        return this.gradeList;
    }

    public void setGradeList(List<GradeEntity> gradeList) {
        this.gradeList = gradeList;
    }
}

package com.capgemini.gradebook.persistence.entity;


import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "CLASS_YEAR")
public class ClassYearEntity extends AbstractEntity {

    private Integer classLevel;
    private String className;
    private String classYear;

    @PreRemove
    public void checkStudentsBeforeRemove() {
        if (!this.studentList.isEmpty()) {
            throw new RuntimeException("Can't remove a ClassYear that has students.");
        }
    }

    @OneToMany(mappedBy = "classYearEntity", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    private List<StudentEntity> studentList;

    @OneToMany(mappedBy = "classYearEntity", cascade = CascadeType.REMOVE)
    private List<SubjectEntity> subjectList;

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


    public List<StudentEntity> getStudentList() {
        return this.studentList;
    }

    public void setStudentList(List<StudentEntity> studentList) {
        this.studentList = studentList;
    }

    public List<SubjectEntity> getSubjectList() {
        return this.subjectList;
    }

    public void setSubjectList(List<SubjectEntity> subjectList) {
        this.subjectList = subjectList;
    }
}

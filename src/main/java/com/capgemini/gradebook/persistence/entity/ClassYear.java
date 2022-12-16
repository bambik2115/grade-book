package com.capgemini.gradebook.persistence.entity;


import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "CLASS_YEAR")
public class ClassYear extends AbstractEntity {

    private Integer classLevel;

    private String className;

    private String classYear;

    @PreRemove
    public void checkStudentsBeforeRemove() {
        if (!this.classYearStudentList.isEmpty()) {
            throw new RuntimeException("Can't remove a ClassYear that has students.");
        }
    }

    @OneToMany(mappedBy = "classYear", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    private List<StudentEntity> classYearStudentList;

    @OneToMany(mappedBy = "classYear", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SubjectEntity> classYearSubjectList;

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

package com.capgemini.gradebook.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "GRADE")
public class Grade extends AbstractEntity {


    private Integer value;

    private Double weight;

    private String gradeType;

    private String comment;

    private LocalDateTime dateOfGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeacherEntity teacherEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectEntity subjectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudentEntity studentEntity;

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getGradeType() {
        return this.gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateOfGrade() {
        return this.dateOfGrade;
    }

    public void setDateOfGrade(LocalDateTime dateOfGrade) {
        this.dateOfGrade = dateOfGrade;
    }

    public TeacherEntity getTeacherEntity() {
        return this.teacherEntity;
    }

    public void setTeacherEntity(TeacherEntity teacherEntity) {
        this.teacherEntity = teacherEntity;
    }

    public SubjectEntity getSubjectEntity() {
        return this.subjectEntity;
    }

    public void setSubjectEntity(SubjectEntity subjectEntity) {
        this.subjectEntity = subjectEntity;
    }

    public StudentEntity getStudentEntity() {
        return this.studentEntity;
    }

    public void setStudentEntity(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }
}

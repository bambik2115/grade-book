package com.krzysztofapp.gradebook.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "GRADE")
public class GradeEntity extends AbstractEntity {


    private Integer value;

    @Column(precision = 3, scale = 2)
    private BigDecimal weight = BigDecimal.ONE;

    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    private String comment;

    private LocalDate dateOfGrade;

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

    public BigDecimal getWeight() {
        return this.weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public GradeType getGradeType() {
        return this.gradeType;
    }

    public void setGradeType(GradeType gradeType) {
        this.gradeType = gradeType;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDateOfGrade() {
        return this.dateOfGrade;
    }

    public void setDateOfGrade(LocalDate dateOfGrade) {
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

package com.capgemini.gradebook.domain;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class GradeEto extends AbstractEto {

    @NotNull
    private Integer value;

    @NotNull
    private Double weight;

    private String gradeType;

    private String comment;

    @NotNull
    private LocalDateTime dateOfGrade;

    private Long teacherEntityId;

    @NotNull
    private Long subjectEntityId;

    @NotNull
    private Long studentEntityId;

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

    public Long getTeacherEntityId() {
        return this.teacherEntityId;
    }

    public void setTeacherEntityId(Long teacherEntityId) {
        this.teacherEntityId = teacherEntityId;
    }

    public Long getSubjectEntityId() {
        return this.subjectEntityId;
    }

    public void setSubjectEntityId(Long subjectEntityId) {
        this.subjectEntityId = subjectEntityId;
    }

    public Long getStudentEntityId() {
        return this.studentEntityId;
    }

    public void setStudentEntityId(Long studentEntityId) {
        this.studentEntityId = studentEntityId;
    }
}

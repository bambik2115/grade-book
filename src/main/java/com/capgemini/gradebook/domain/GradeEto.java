package com.capgemini.gradebook.domain;

import com.capgemini.gradebook.persistence.entity.GradeType;
import org.hibernate.validator.constraints.Range;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GradeEto extends AbstractEto {

    @NotNull
    @Range(min = 1, max = 6, message = "Grade value must be between 1 and 6")
    private Integer value;

    @Range(min = 1, max = 6, message = "Grade weight must be between 1 and 6")
    private BigDecimal weight;

    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

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

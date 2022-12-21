package com.capgemini.gradebook.domain;

import com.capgemini.gradebook.persistence.entity.GradeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GradeSearchCriteria {

    Integer valueFrom;

    Integer valueTo;

    BigDecimal weightFrom;

    BigDecimal weightTo;

    LocalDateTime createdDateFrom;

    LocalDateTime createdDateTo;

    GradeType gradeType;

    Long subjectEntityId;

    Long studentEntityId;

    public Integer getValueFrom() {
        return this.valueFrom;
    }

    public void setValueFrom(Integer valueFrom) {
        this.valueFrom = valueFrom;
    }

    public Integer getValueTo() {
        return this.valueTo;
    }

    public void setValueTo(Integer valueTo) {
        this.valueTo = valueTo;
    }

    public BigDecimal getWeightFrom() {
        return this.weightFrom;
    }

    public void setWeightFrom(BigDecimal weightFrom) {
        this.weightFrom = weightFrom;
    }

    public BigDecimal getWeightTo() {
        return this.weightTo;
    }

    public void setWeightTo(BigDecimal weightTo) {
        this.weightTo = weightTo;
    }

    public LocalDateTime getCreatedDateFrom() {
        return this.createdDateFrom;
    }

    public void setCreatedDateFrom(LocalDateTime createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public LocalDateTime getCreatedDateTo() {
        return this.createdDateTo;
    }

    public void setCreatedDateTo(LocalDateTime createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public GradeType getGradeType() {
        return this.gradeType;
    }

    public void setGradeType(GradeType gradeType) {
        this.gradeType = gradeType;
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

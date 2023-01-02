package com.capgemini.gradebook.domain;

import com.capgemini.gradebook.persistence.entity.SubjectType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class SubjectEto extends AbstractEto {

    @Enumerated(EnumType.STRING)
    @NotNull
    private SubjectType subjectType;

    @NotNull
    private Long teacherEntityId;

    @NotNull
    private Long classYearId;

    public SubjectType getSubjectType() {
        return this.subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public Long getTeacherEntityId() {
        return this.teacherEntityId;
    }

    public void setTeacherEntityId(Long teacherEntityId) {
        this.teacherEntityId = teacherEntityId;
    }

    public Long getClassYearId() {
        return this.classYearId;
    }

    public void setClassYearId(Long classYearId) {
        this.classYearId = classYearId;
    }
}

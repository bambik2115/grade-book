package com.capgemini.gradebook.domain;

import com.capgemini.gradebook.persistence.entity.SubjectType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class SubjectEto extends AbstractEto {

    @NotNull
    private SubjectType subjectType;

    @NotNull
    private Long teacherEntityId;

    @NotNull
    private Long classYearEntityId;

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

    public Long getClassYearEntityId() {
        return this.classYearEntityId;
    }

    public void setClassYearEntityId(Long classYearId) {
        this.classYearEntityId = classYearId;
    }
}

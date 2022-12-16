package com.capgemini.gradebook.domain.mapper;

import com.capgemini.gradebook.domain.SubjectEto;
import com.capgemini.gradebook.persistence.entity.SubjectEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class SubjectMapper {


    public static final SubjectEto mapToETO(SubjectEntity entity){

        SubjectEto subject = new SubjectEto();
        subject.setId(entity.getId());
        subject.setVersion(entity.getVersion());
        subject.setCreateDate(entity.getCreateDate());
        subject.setUpdateDate(entity.getUpdateDate());
        subject.setSubjectType(entity.getSubjectType());
        subject.setTeacherEntityId(entity.getTeacherEntity().getId());
        subject.setClassYearId(entity.getClassYear().getId());

        return subject;
    }

    public static final SubjectEntity mapToEntity(SubjectEto subjectTo){

        SubjectEntity entity = new SubjectEntity();
        entity.setId(subjectTo.getId());
        entity.setVersion(subjectTo.getVersion());
        entity.setCreateDate(subjectTo.getCreateDate());
        entity.setUpdateDate(subjectTo.getUpdateDate());
        entity.setSubjectType(subjectTo.getSubjectType());

        return entity;
    }

    public static final List<SubjectEto> mapToETOList(List<SubjectEntity> entities){
        return entities.stream().map(e -> mapToETO(e)).collect(Collectors.toList());
    }

    public static final List<SubjectEntity> mapToEntityList(List<SubjectEto> tos){
        return tos.stream().map(t -> mapToEntity(t)).collect(Collectors.toList());
    }


}

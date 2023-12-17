package com.krzysztofapp.gradebook.domain.mapper;

import com.krzysztofapp.gradebook.domain.SubjectEto;
import com.krzysztofapp.gradebook.persistence.entity.SubjectEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class SubjectMapper {


    public static final SubjectEto mapToETO(SubjectEntity entity){

        SubjectEto subjectTo = new SubjectEto();
        subjectTo.setId(entity.getId());
        subjectTo.setVersion(entity.getVersion());
        subjectTo.setCreateDate(entity.getCreateDate());
        subjectTo.setUpdateDate(entity.getUpdateDate());
        subjectTo.setSubjectType(entity.getSubjectType());
        subjectTo.setTeacherEntityId(entity.getTeacherEntity().getId());
        subjectTo.setClassYearEntityId(entity.getClassYear().getId());

        return subjectTo;
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

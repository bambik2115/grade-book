package com.krzysztofapp.gradebook.domain.mapper;

import com.krzysztofapp.gradebook.domain.GradeEto;
import com.krzysztofapp.gradebook.persistence.entity.GradeEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class GradeMapper {

    public static final GradeEto mapToETO(GradeEntity grade){

        GradeEto gradeTo = new GradeEto();
        gradeTo.setId(grade.getId());
        gradeTo.setVersion(grade.getVersion());
        gradeTo.setCreateDate(grade.getCreateDate());
        gradeTo.setUpdateDate(grade.getUpdateDate());
        gradeTo.setGradeType(grade.getGradeType());
        gradeTo.setComment(grade.getComment());
        gradeTo.setValue(grade.getValue());
        gradeTo.setWeight(grade.getWeight());
        gradeTo.setDateOfGrade(grade.getDateOfGrade());
        gradeTo.setStudentEntityId(grade.getStudentEntity().getId());
        gradeTo.setSubjectEntityId(grade.getSubjectEntity().getId());
        gradeTo.setTeacherEntityId(grade.getTeacherEntity().getId());


        return gradeTo;
    }

    public static final GradeEntity mapToEntity(GradeEto gradeTo){

        GradeEntity grade = new GradeEntity();
        grade.setId(gradeTo.getId());
        grade.setVersion(gradeTo.getVersion());
        grade.setCreateDate(gradeTo.getCreateDate());
        grade.setUpdateDate(gradeTo.getUpdateDate());
        grade.setGradeType(gradeTo.getGradeType());
        grade.setComment(gradeTo.getComment());
        grade.setValue(gradeTo.getValue());
        grade.setWeight(gradeTo.getWeight());
        grade.setDateOfGrade(gradeTo.getDateOfGrade());

        return grade;
    }

    public static final List<GradeEto> mapToETOList(List<GradeEntity> entities){
        return entities.stream().map(e -> mapToETO(e)).collect(Collectors.toList());
    }

    public static final List<GradeEntity> mapToEntityList(List<GradeEto> tos){
        return tos.stream().map(t -> mapToEntity(t)).collect(Collectors.toList());
    }

}

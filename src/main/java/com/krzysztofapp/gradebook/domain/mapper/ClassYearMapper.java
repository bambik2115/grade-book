package com.krzysztofapp.gradebook.domain.mapper;

import com.krzysztofapp.gradebook.domain.ClassYearEto;
import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class ClassYearMapper {


    public static final ClassYearEto mapToETO(ClassYearEntity entity){

        ClassYearEto classYear = new ClassYearEto();
        classYear.setId(entity.getId());
        classYear.setVersion(entity.getVersion());
        classYear.setCreateDate(entity.getCreateDate());
        classYear.setUpdateDate(entity.getUpdateDate());
        classYear.setClassLevel(entity.getClassLevel());
        classYear.setClassName(entity.getClassName());
        classYear.setClassYear(entity.getClassYear());
        return classYear;
    }

    public static final ClassYearEntity mapToEntity(ClassYearEto classYearTo){

        ClassYearEntity entity = new ClassYearEntity();
        entity.setId(classYearTo.getId());
        entity.setVersion(classYearTo.getVersion());
        entity.setCreateDate(classYearTo.getCreateDate());
        entity.setUpdateDate(classYearTo.getUpdateDate());
        entity.setClassLevel(classYearTo.getClassLevel());
        entity.setClassName(classYearTo.getClassName());
        entity.setClassYear(classYearTo.getClassYear());
        return entity;
    }


    public static final List<ClassYearEto> mapToETOList(List<ClassYearEntity> entities){
        return entities.stream().map(e -> mapToETO(e)).collect(Collectors.toList());
    }

    public static final List<ClassYearEntity> mapToEntityList(List<ClassYearEto> tos){
        return tos.stream().map(t -> mapToEntity(t)).collect(Collectors.toList());
    }




}

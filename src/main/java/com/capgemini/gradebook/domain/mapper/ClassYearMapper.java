package com.capgemini.gradebook.domain.mapper;

import com.capgemini.gradebook.domain.ClassYearEto;
import com.capgemini.gradebook.persistence.entity.ClassYear;

import java.util.List;
import java.util.stream.Collectors;

public final class ClassYearMapper {


    public static final ClassYearEto mapToETO(ClassYear entity){

        ClassYearEto classyear = new ClassYearEto();
        classyear.setId(entity.getId());
        classyear.setVersion(entity.getVersion());
        classyear.setCreateDate(entity.getCreateDate());
        classyear.setUpdateDate(entity.getUpdateDate());
        classyear.setClassLevel(entity.getClassLevel());
        classyear.setClassName(entity.getClassName());
        classyear.setClassYear(entity.getClassYear());
        return classyear;
    }

    public static final ClassYear mapToEntity(ClassYearEto classyearTo){

        ClassYear entity = new ClassYear();
        entity.setId(classyearTo.getId());
        entity.setVersion(classyearTo.getVersion());
        entity.setCreateDate(classyearTo.getCreateDate());
        entity.setUpdateDate(classyearTo.getUpdateDate());
        entity.setClassLevel(classyearTo.getClassLevel());
        entity.setClassName(classyearTo.getClassName());
        entity.setClassYear(classyearTo.getClassYear());
        return entity;
    }


    public static final List<ClassYearEto> mapToETOList(List<ClassYear> entities){
        return entities.stream().map(e -> mapToETO(e)).collect(Collectors.toList());
    }

    public static final List<ClassYear> mapToEntityList(List<ClassYearEto> tos){
        return tos.stream().map(t -> mapToEntity(t)).collect(Collectors.toList());
    }




}

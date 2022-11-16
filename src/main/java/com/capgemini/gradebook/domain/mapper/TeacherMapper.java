package com.capgemini.gradebook.domain.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.capgemini.gradebook.domain.TeacherEto;
import com.capgemini.gradebook.persistence.entity.TeacherEntity;

public final class TeacherMapper {

  public static final TeacherEto mapToETO(TeacherEntity entity){

    TeacherEto teacher = new TeacherEto();
    teacher.setId(entity.getId());
    teacher.setVersion(entity.getVersion());
    teacher.setCreateDate(entity.getCreateDate());
    teacher.setUpdateDate(entity.getUpdateDate());
    teacher.setFirstName(entity.getFirstName());
    teacher.setLastName(entity.getLastName());
    return teacher;
  }

  public static final TeacherEntity mapToEntity(TeacherEto teacherTo){

    TeacherEntity entity = new TeacherEntity();
    entity.setId(teacherTo.getId());
    entity.setVersion(teacherTo.getVersion());
    entity.setCreateDate(teacherTo.getCreateDate());
    entity.setUpdateDate(teacherTo.getUpdateDate());
    entity.setFirstName(teacherTo.getFirstName());
    entity.setLastName(teacherTo.getLastName());
    return entity;
  }

  public static final List<TeacherEto> mapToETOList(List<TeacherEntity> entities){
    return entities.stream().map(e -> mapToETO(e)).collect(Collectors.toList());
  }

  public static final List<TeacherEntity> mapToEntityList(List<TeacherEto> tos){
    return tos.stream().map(t -> mapToEntity(t)).collect(Collectors.toList());
  }

}

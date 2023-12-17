package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.persistence.entity.GradeType;
import com.capgemini.gradebook.persistence.entity.StudentEntity;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepoCustom {

    List<StudentEntity> findAllByGradeFAtCertainDay(LocalDate day);

    List<StudentEntity> findAllByCertainGradeAtCertainDay(GradeType grade, LocalDate date);
}

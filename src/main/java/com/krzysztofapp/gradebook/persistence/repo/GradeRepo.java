package com.krzysztofapp.gradebook.persistence.repo;

import com.krzysztofapp.gradebook.persistence.entity.GradeEntity;
import com.krzysztofapp.gradebook.persistence.entity.GradeType;
import com.krzysztofapp.gradebook.persistence.repo.custom.GradeRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GradeRepo extends JpaRepository<GradeEntity, Long>, GradeRepoCustom, JpaSpecificationExecutor<GradeEntity> {


    Optional<GradeEntity> findGradeByDateOfGradeAndGradeType(LocalDate dateOfGrade, GradeType gradeType);

    List<GradeEntity> findAllGradeByStudentEntityIdAndSubjectEntityId(Long studentId, Long subjectId);

    List<GradeEntity> findAllByTeacherEntityIdIsNull();

    List<GradeEntity> findAllBySubjectEntityId(Long id);

    List<GradeEntity> findAllByStudentEntityId(Long id);




}

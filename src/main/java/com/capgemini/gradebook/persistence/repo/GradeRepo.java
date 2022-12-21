package com.capgemini.gradebook.persistence.repo;

import com.capgemini.gradebook.persistence.entity.Grade;
import com.capgemini.gradebook.persistence.entity.GradeType;
import com.capgemini.gradebook.persistence.repo.custom.GradeRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GradeRepo extends JpaRepository<Grade, Long>, GradeRepoCustom, JpaSpecificationExecutor<Grade> {


    Optional<Grade> findGradeByDateOfGradeGreaterThanEqualAndGradeType(LocalDateTime dateOfGrade, GradeType gradeType);

    List<Grade> findAllGradeByStudentEntityIdAndSubjectEntityId(Long studentId, Long subjectId);

    List<Grade> findBySubjectEntityIdIsNull();

    List<Grade> findByTeacherEntityIdIsNull();


}

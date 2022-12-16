package com.capgemini.gradebook.persistence.repo;

import com.capgemini.gradebook.persistence.entity.Grade;
import com.capgemini.gradebook.persistence.repo.custom.GradeRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepo extends JpaRepository<Grade, Long>, GradeRepoCustom {

    List<Grade> findBySubjectEntityIdIsNull();

    List<Grade> findByTeacherEntityIdIsNull();
}

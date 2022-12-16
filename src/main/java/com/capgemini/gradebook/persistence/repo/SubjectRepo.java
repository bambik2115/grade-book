package com.capgemini.gradebook.persistence.repo;

import com.capgemini.gradebook.persistence.entity.SubjectEntity;
import com.capgemini.gradebook.persistence.repo.custom.SubjectRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubjectRepo extends JpaRepository<SubjectEntity, Long>, SubjectRepoCustom {

    List<SubjectEntity> findByTeacherEntityIdIsNull();

    List<SubjectEntity> findAllStudentEntityByClassYearId(Long id);
}

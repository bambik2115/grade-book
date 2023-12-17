package com.krzysztofapp.gradebook.persistence.repo;

import com.krzysztofapp.gradebook.persistence.entity.SubjectEntity;
import com.krzysztofapp.gradebook.persistence.repo.custom.SubjectRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubjectRepo extends JpaRepository<SubjectEntity, Long>, SubjectRepoCustom {

    List<SubjectEntity> findAllByTeacherEntityIdIsNull();

    List<SubjectEntity> findAllStudentEntityByClassYearEntityId(Long id);

    List<SubjectEntity> findAllByTeacherEntityId(Long id);
}

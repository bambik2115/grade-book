package com.capgemini.gradebook.persistence.repo;

import com.capgemini.gradebook.persistence.entity.StudentEntity;
import com.capgemini.gradebook.persistence.repo.custom.StudentRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<StudentEntity, Long>, StudentRepoCustom {
}

package com.krzysztofapp.gradebook.persistence.repo;

import com.krzysztofapp.gradebook.persistence.entity.StudentEntity;
import com.krzysztofapp.gradebook.persistence.repo.custom.StudentRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<StudentEntity, Long>, StudentRepoCustom {
}

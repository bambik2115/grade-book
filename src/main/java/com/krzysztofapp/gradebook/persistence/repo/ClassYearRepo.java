package com.krzysztofapp.gradebook.persistence.repo;

import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;
import com.krzysztofapp.gradebook.persistence.repo.custom.ClassYearRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClassYearRepo extends JpaRepository<ClassYearEntity, Long>, ClassYearRepoCustom {

}

package com.capgemini.gradebook.persistence.repo;

import com.capgemini.gradebook.persistence.entity.ClassYear;
import com.capgemini.gradebook.persistence.repo.custom.ClassYearRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClassYearRepo extends JpaRepository<ClassYear, Long>, ClassYearRepoCustom {

}

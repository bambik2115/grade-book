package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.domain.GradeSearchCriteria;
import com.capgemini.gradebook.persistence.entity.Grade;
import com.capgemini.gradebook.persistence.entity.GradeType;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GradeRepoCustom {

    List<Grade> findByCriteria(GradeSearchCriteria criteria);

}

package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.domain.GradeSearchCriteria;
import com.capgemini.gradebook.persistence.entity.Grade;

import java.util.List;

public interface GradeRepoCustom {

    List<Grade> findByCriteria(GradeSearchCriteria criteria);

}

package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.domain.GradeSearchCriteria;
import com.capgemini.gradebook.persistence.entity.GradeEntity;

import java.util.List;

public interface GradeRepoCustom {

    List<GradeEntity> findByCriteria(GradeSearchCriteria criteria);

}

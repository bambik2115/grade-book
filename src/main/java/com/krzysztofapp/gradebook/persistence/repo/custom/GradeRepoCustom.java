package com.krzysztofapp.gradebook.persistence.repo.custom;

import com.krzysztofapp.gradebook.domain.GradeSearchCriteria;
import com.krzysztofapp.gradebook.persistence.entity.GradeEntity;

import java.util.List;

public interface GradeRepoCustom {

    List<GradeEntity> findByCriteria(GradeSearchCriteria criteria);

}

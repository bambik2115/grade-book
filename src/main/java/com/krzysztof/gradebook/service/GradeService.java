package com.capgemini.gradebook.service;

import com.capgemini.gradebook.domain.GradeEto;
import com.capgemini.gradebook.domain.GradeSearchCriteria;

import java.util.List;
import java.util.Map;

public interface GradeService {

    GradeEto findGradeById(Long id);

    Double getWeightedAverage(Long studentId, Long subjectId);

    List<GradeEto> searchGradesByCriteria(GradeSearchCriteria criteria);

    GradeEto createNew(GradeEto newGrade);

    void delete(Long id);

    GradeEto partialUpdate(Long id, Map<String, Object> updateInfo);

}

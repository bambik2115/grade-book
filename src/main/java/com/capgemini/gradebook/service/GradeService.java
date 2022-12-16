package com.capgemini.gradebook.service;

import com.capgemini.gradebook.domain.GradeEto;

import java.util.Map;

public interface GradeService {

    GradeEto findGradeById(Long id);

    GradeEto createNew(GradeEto newGrade);

    void delete(Long id);

    GradeEto partialUpdate(Long id, Map<String, Object> updateInfo);

}

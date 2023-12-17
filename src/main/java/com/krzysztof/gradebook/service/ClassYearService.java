package com.capgemini.gradebook.service;

import com.capgemini.gradebook.domain.ClassYearEto;

import java.util.Map;

public interface ClassYearService {

    ClassYearEto findClassYearById(Long id);

    ClassYearEto createNew(ClassYearEto newClassYear);

    ClassYearEto partialUpdate(Long id, Map<String, Object> updateInfo);

    void delete(Long id);
}

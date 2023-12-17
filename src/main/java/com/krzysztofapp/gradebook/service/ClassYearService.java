package com.krzysztofapp.gradebook.service;

import com.krzysztofapp.gradebook.domain.ClassYearEto;

import java.util.Map;

public interface ClassYearService {

    ClassYearEto findClassYearById(Long id);

    ClassYearEto createNew(ClassYearEto newClassYear);

    ClassYearEto partialUpdate(Long id, Map<String, Object> updateInfo);

    void delete(Long id);
}

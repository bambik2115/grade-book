package com.capgemini.gradebook.service;

import com.capgemini.gradebook.domain.StudentEto;

import java.util.Map;

public interface StudentService {


    StudentEto findStudentById(Long id);

    StudentEto createNew(StudentEto newStudent);

    StudentEto partialUpdate(Long id, Map<String, Object> updateInfo);

    void delete(Long id);
}

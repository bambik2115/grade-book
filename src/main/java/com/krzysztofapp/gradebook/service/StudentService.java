package com.krzysztofapp.gradebook.service;

import com.krzysztofapp.gradebook.domain.GradeContext;
import com.krzysztofapp.gradebook.domain.StudentEto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StudentService {

    List<StudentEto> findAllStudentsWithGradeFAtCertainDay(LocalDate day);

    StudentEto findStudentById(Long id);

    Integer getNumberOfStudents(GradeContext context);

    StudentEto createNew(StudentEto newStudent);

    StudentEto partialUpdate(Long id, Map<String, Object> updateInfo);

    void delete(Long id);
}

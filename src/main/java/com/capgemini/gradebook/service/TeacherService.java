package com.capgemini.gradebook.service;

import com.capgemini.gradebook.domain.TeacherEto;

import java.util.List;
import java.util.Map;

public interface TeacherService {

	List<TeacherEto> findAllTeachers();

	List<TeacherEto> findTeacherByLastName(final String lastname) ;

	List<String> getSubjects(Long id);

	TeacherEto findTeacherById(Long id);

	TeacherEto partialUpdate(Long id, Map<String, Object> updateInfo);

	TeacherEto createNew(TeacherEto newTeacher);

	void delete(Long id, Long newTeacherId);
}

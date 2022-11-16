package com.capgemini.gradebook.persistence.repo;

import java.util.List;

import com.capgemini.gradebook.persistence.entity.TeacherEntity;

public interface TeacherRepoCustom {

	List<TeacherEntity> findTeacherByLastName(String lastName);
}

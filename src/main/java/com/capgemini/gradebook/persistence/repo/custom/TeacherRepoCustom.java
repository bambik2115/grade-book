package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.persistence.entity.TeacherEntity;

import java.util.List;

public interface TeacherRepoCustom {

	List<TeacherEntity> findTeacherByLastName(String lastName);
}

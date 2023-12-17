package com.krzysztofapp.gradebook.persistence.repo.custom;

import com.krzysztofapp.gradebook.persistence.entity.TeacherEntity;

import java.util.List;

public interface TeacherRepoCustom {

	List<TeacherEntity> findTeachersByLastName(String lastName);
}

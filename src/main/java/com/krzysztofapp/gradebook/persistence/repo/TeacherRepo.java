package com.krzysztofapp.gradebook.persistence.repo;

import com.krzysztofapp.gradebook.persistence.entity.TeacherEntity;
import com.krzysztofapp.gradebook.persistence.repo.custom.TeacherRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

//Dev Note: using this, a spring will create a proxy TeacherRepo and automatically use the TeacherRepoCustom methods,
// with implementations provided by its TeacherRepoCustomImpl

public interface TeacherRepo extends JpaRepository<TeacherEntity, Long>, TeacherRepoCustom {

//Dev Note: you can add queries using annotation instead of repository
//  @Query("SELECT t FROM TeacherEntity t WHERE t.lastName = :lastName")
//  TeacherEntity getTeacherByLastname(@Param("lastName") String lastName);



}

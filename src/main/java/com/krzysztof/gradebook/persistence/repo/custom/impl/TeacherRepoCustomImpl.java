package com.capgemini.gradebook.persistence.repo.custom.impl;

import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.custom.TeacherRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class TeacherRepoCustomImpl implements TeacherRepoCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<TeacherEntity> findTeachersByLastName(String lastName) {

    List<TeacherEntity> result = em.createQuery("SELECT t FROM TeacherEntity t WHERE t.lastName = :lastName")
        .setParameter("lastName", lastName)
        .getResultList();

    return result;
  }
}

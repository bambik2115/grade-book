package com.capgemini.gradebook.persistence.repo.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.TeacherRepoCustom;

public class TeacherRepoCustomImpl implements TeacherRepoCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<TeacherEntity> findTeacherByLastName(String lastName) {

    List<TeacherEntity> result = em.createQuery("SELECT t FROM TeacherEntity t WHERE t.lastName = :lastName")
        .setParameter("lastName", lastName)
        .getResultList();

    return result;
  }
}

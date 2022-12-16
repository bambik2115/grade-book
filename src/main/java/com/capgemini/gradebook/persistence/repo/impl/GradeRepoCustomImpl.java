package com.capgemini.gradebook.persistence.repo.impl;

import com.capgemini.gradebook.persistence.repo.custom.GradeRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GradeRepoCustomImpl implements GradeRepoCustom {


    @PersistenceContext
    private EntityManager em;



}

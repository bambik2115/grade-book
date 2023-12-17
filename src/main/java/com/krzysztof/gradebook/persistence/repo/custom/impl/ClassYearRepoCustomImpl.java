package com.capgemini.gradebook.persistence.repo.custom.impl;

import com.capgemini.gradebook.persistence.repo.custom.ClassYearRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ClassYearRepoCustomImpl implements ClassYearRepoCustom {

    @PersistenceContext
    private EntityManager em;


}

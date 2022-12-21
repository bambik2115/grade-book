package com.capgemini.gradebook.persistence.repo.custom.impl;

import com.capgemini.gradebook.persistence.repo.custom.StudentRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class StudentRepoCustomImpl implements StudentRepoCustom {


    @PersistenceContext
    private EntityManager em;

}

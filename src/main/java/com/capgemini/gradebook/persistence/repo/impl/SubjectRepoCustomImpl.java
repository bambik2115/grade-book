package com.capgemini.gradebook.persistence.repo.impl;

import com.capgemini.gradebook.persistence.repo.custom.SubjectRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SubjectRepoCustomImpl implements SubjectRepoCustom {

    @PersistenceContext
    private EntityManager em;


}

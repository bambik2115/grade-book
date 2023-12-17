package com.krzysztofapp.gradebook.persistence.repo.custom.impl;

import com.krzysztofapp.gradebook.persistence.repo.custom.SubjectRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SubjectRepoCustomImpl implements SubjectRepoCustom {

    @PersistenceContext
    private EntityManager em;


}

package com.capgemini.gradebook.persistence.repo.custom.impl;

import com.capgemini.gradebook.domain.GradeSearchCriteria;
import com.capgemini.gradebook.persistence.entity.Grade;
import com.capgemini.gradebook.persistence.entity.GradeType;
import com.capgemini.gradebook.persistence.repo.custom.GradeRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeRepoCustomImpl implements GradeRepoCustom {


    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Grade> findByCriteria(GradeSearchCriteria criteria) {

        CriteriaBuilder query = em.getCriteriaBuilder();
        CriteriaQuery criteriaquery = query.createQuery();
        Root<Grade> grade = criteriaquery.from(Grade.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if(criteria.getGradeType() != null){
            predicates.add(query.equal(grade.get("gradeType"), criteria.getGradeType()));
        }

        criteriaquery.select(grade)
                .where(predicates.toArray(new Predicate[0]));

        List<Grade> result = em.createQuery(criteriaquery).getResultList();

        return result;
    }

}

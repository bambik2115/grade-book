package com.krzysztofapp.gradebook.persistence.repo.custom.impl;

import com.krzysztofapp.gradebook.domain.GradeSearchCriteria;
import com.krzysztofapp.gradebook.persistence.entity.GradeEntity;
import com.krzysztofapp.gradebook.persistence.repo.custom.GradeRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class GradeRepoCustomImpl implements GradeRepoCustom {


    @PersistenceContext
    private EntityManager em;


    @Override
    public List<GradeEntity> findByCriteria(GradeSearchCriteria criteria) {

        CriteriaBuilder query = em.getCriteriaBuilder();
        CriteriaQuery criteriaquery = query.createQuery();
        Root<GradeEntity> grade = criteriaquery.from(GradeEntity.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if(criteria.getGradeType() != null) {
            predicates.add(query.equal(grade.get("gradeType"), criteria.getGradeType()));
        }
        if(criteria.getStudentEntityId() != null) {
            predicates.add(query.equal(grade.get("studentEntity"), criteria.getStudentEntityId()));
        }
        if(criteria.getSubjectEntityId() != null) {
            predicates.add(query.equal(grade.get("subjectEntity"), criteria.getSubjectEntityId()));
        }
        if(criteria.getValueFrom() != null) {
            predicates.add(query.greaterThanOrEqualTo(grade.get("value"), criteria.getValueFrom()));
        }
        if(criteria.getValueTo() != null) {
            predicates.add(query.lessThanOrEqualTo(grade.get("value"), criteria.getValueTo()));
        }
        if(criteria.getCreatedDateFrom() != null) {
            predicates.add(query.greaterThanOrEqualTo(grade.get("dateOfGrade"), criteria.getCreatedDateFrom()));
        }
        if(criteria.getCreatedDateTo() != null) {
            predicates.add(query.lessThanOrEqualTo(grade.get("dateOfGrade"), criteria.getCreatedDateTo()));
        }
        if(criteria.getWeightFrom() != null) {
            predicates.add(query.greaterThanOrEqualTo(grade.get("weight"), criteria.getWeightFrom()));
        }
        if(criteria.getWeightTo() != null) {
            predicates.add(query.lessThanOrEqualTo(grade.get("weight"), criteria.getWeightTo()));
        }

        criteriaquery.select(grade)
                .where(predicates.toArray(new Predicate[0]));

        List<GradeEntity> result = em.createQuery(criteriaquery).getResultList();

        return result;
    }

}

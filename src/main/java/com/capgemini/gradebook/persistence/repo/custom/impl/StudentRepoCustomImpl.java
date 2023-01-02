package com.capgemini.gradebook.persistence.repo.custom.impl;

import com.capgemini.gradebook.persistence.entity.GradeType;
import com.capgemini.gradebook.persistence.entity.StudentEntity;
import com.capgemini.gradebook.persistence.repo.custom.StudentRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

public class StudentRepoCustomImpl implements StudentRepoCustom {


    @PersistenceContext
    private EntityManager em;


    @Override
    public List<StudentEntity> findAllByGradeFAtCertainDay(LocalDate day) {

        List<StudentEntity> result = em.createQuery("SELECT s FROM StudentEntity s JOIN s.studentGradeList gl WHERE gl.gradeType = :F AND gl.dateOfGrade = :day")
                .setParameter("day", day)
                .setParameter("F", GradeType.F)
                .getResultList();
        return result;
    }

    @Override
    public List<StudentEntity> findAllByCertainGradeAtCertainDay(GradeType grade, LocalDate day) {

        List<StudentEntity> result = em.createQuery("SELECT s FROM StudentEntity s JOIN s.studentGradeList gl WHERE gl.gradeType = :grade AND gl.dateOfGrade = :day")
                .setParameter("grade", grade)
                .setParameter("day", day)
                .getResultList();
        return result;
    }
}

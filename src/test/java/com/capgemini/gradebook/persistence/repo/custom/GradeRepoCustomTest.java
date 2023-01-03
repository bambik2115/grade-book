package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.domain.GradeSearchCriteria;
import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@SpringBootTest
class GradeRepoCustomTest {

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private TestEntityCreator tec;

    @Inject
    private GradeRepo grepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }


    @Test
    public void findByCriteriaShouldReturnMatchingGrades() {
        //Given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.E, 2, LocalDate.parse("2022-11-12"), BigDecimal.valueOf(3.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-14"), BigDecimal.valueOf(4.00));
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(3.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-12"), BigDecimal.valueOf(3.00));

        GradeSearchCriteria criteria = new GradeSearchCriteria();
        criteria.setGradeType(GradeType.D);
        criteria.setValueFrom(3);
        criteria.setValueTo(4);
        criteria.setWeightFrom(BigDecimal.valueOf(2.00));
        criteria.setWeightTo(BigDecimal.valueOf(4.00));
        criteria.setCreatedDateFrom(LocalDate.parse("2022-11-11"));
        criteria.setCreatedDateTo(LocalDate.parse("2022-12-01"));
        criteria.setStudentEntityId(1L);
        criteria.setSubjectEntityId(1L);

        //When
        List<Grade> result = this.grepo.findByCriteria(criteria);

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(5L);
        Assertions.assertThat(result.get(0).getDateOfGrade()).isEqualTo(LocalDate.parse("2022-11-16"));
        Assertions.assertThat(result.get(1).getDateOfGrade()).isEqualTo(LocalDate.parse("2022-11-12"));
    }

    @Test
    public void findByCriteriaShouldReturnMatchingDateRangeResults() {
        //Given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.E, 2, LocalDate.parse("2022-11-11"), BigDecimal.valueOf(3.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(4.00));
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-29"), BigDecimal.valueOf(3.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-30"), BigDecimal.valueOf(3.00));

        GradeSearchCriteria criteria = new GradeSearchCriteria();
        criteria.setCreatedDateFrom(LocalDate.parse("2022-11-12"));
        criteria.setCreatedDateTo(LocalDate.parse("2022-11-29"));

        //When
        List<Grade> result = this.grepo.findByCriteria(criteria);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(3L);
        Assertions.assertThat(result.get(2).getDateOfGrade()).isEqualTo(LocalDate.parse("2022-11-29"));
    }

    @Test
    public void findByCriteriaShouldReturnMatchingWeightRangeResults() {
        //Given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.E, 2, LocalDate.parse("2022-11-11"), BigDecimal.valueOf(1.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(4.00));
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-29"), BigDecimal.valueOf(5.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-30"), BigDecimal.valueOf(3.00));

        GradeSearchCriteria criteria = new GradeSearchCriteria();
        criteria.setWeightFrom(BigDecimal.valueOf(2.00));
        criteria.setWeightTo(BigDecimal.valueOf(4.00));

        //When
        List<Grade> result = this.grepo.findByCriteria(criteria);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(3L);
        Assertions.assertThat(result.get(2).getWeight()).isEqualTo(BigDecimal.valueOf(3.00));
    }

    @Test
    public void findByCriteriaShouldReturnMatchingValueRangeResults() {
        //Given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.E, 2, LocalDate.parse("2022-11-11"), BigDecimal.valueOf(3.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(4.00));
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-29"), BigDecimal.valueOf(3.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-30"), BigDecimal.valueOf(3.00));

        GradeSearchCriteria criteria = new GradeSearchCriteria();
        criteria.setValueFrom(3);
        criteria.setValueTo(4);

        //When
        List<Grade> result = this.grepo.findByCriteria(criteria);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(2).getValue()).isEqualTo(3);
    }

    private void createGrade(TeacherEntity te, StudentEntity ste, SubjectEntity sue, GradeType gt, Integer val, LocalDate date, BigDecimal wg) {
        Grade grade = new Grade();
        grade.setSubjectEntity(sue);
        grade.setStudentEntity(ste);
        grade.setTeacherEntity(te);
        grade.setGradeType(gt);
        grade.setValue(val);
        grade.setDateOfGrade(date);
        grade.setWeight(wg);
        this.grepo.save(grade);
    }



}
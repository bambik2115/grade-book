package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import com.capgemini.gradebook.persistence.repo.StudentRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class StudentRepoCustomTest {

    @Inject
    private StudentRepo studentRepo;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private TestEntityCreator tec;

    @Inject
    private GradeRepo grepo;

    @Inject
    private StudentRepo strepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }

    @Test
    public void findByGradeFAtCertainDayShouldReturnMatchingStudents() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        StudentEntity ste1 = tec.saveTestStudent(cy);
        StudentEntity ste2 = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));
        createGrade(te, ste2, sue, GradeType.D, 3, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(2.00));


        List<StudentEntity> result = this.strepo.findAllByGradeFAtCertainDay(LocalDate.parse("2022-11-16"));

        Assertions.assertThat(result.size()).isEqualTo(2L);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(0).getFirstName()).isEqualTo("Kamil");
    }

    @Test
    public void findByGradeFAtCertainDayShouldReturnEmptyListIfNoMatches() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        StudentEntity ste1 = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));


        List<StudentEntity> result = this.strepo.findAllByGradeFAtCertainDay(LocalDate.parse("2022-11-17"));

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void findByCertainGradeAtCertainDayShouldReturnMatchingStudents() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        StudentEntity ste1 = tec.saveTestStudent(cy);
        StudentEntity ste2 = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.B, 5, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));
        createGrade(te, ste2, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(2.00));


        List<StudentEntity> result = this.strepo.findAllByCertainGradeAtCertainDay(GradeType.C, LocalDate.parse("2022-11-18"));

        Assertions.assertThat(result.size()).isEqualTo(2L);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(3L);
        Assertions.assertThat(result.get(0).getFirstName()).isEqualTo("Kamil");
    }

    @Test
    public void findByCertainGradeAtCertainDayShouldReturnEmptyListIfNoMatches() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        StudentEntity ste1 = tec.saveTestStudent(cy);
        StudentEntity ste2 = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.B, 5, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));
        createGrade(te, ste2, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(2.00));


        List<StudentEntity> result = this.strepo.findAllByCertainGradeAtCertainDay(GradeType.D, LocalDate.parse("2022-11-18"));

        Assertions.assertThat(result).isEmpty();
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
package com.capgemini.gradebook.persistence.repo.custom;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.domain.GradeEto;
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
import java.util.stream.Collectors;

@SpringBootTest
class StudentRepoCustomTest extends TestEntityCreator {

    @Inject
    private StudentRepo studentRepo;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private GradeRepo gRepo;

    @Inject
    private StudentRepo stRepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }

    @Test
    public void findByGradeFAtCertainDayShouldReturnMatchingStudents() {
        //Given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy, te);
        StudentEntity ste = saveTestStudent(cy);
        StudentEntity ste1 = saveTestStudent(cy);
        StudentEntity ste2 = saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));
        createGrade(te, ste2, sue, GradeType.D, 3, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(2.00));

        //When
        List<StudentEntity> result = stRepo.findAllByGradeFAtCertainDay(LocalDate.parse("2022-11-16"));

        List<Long> listOfIds = result.stream().map(StudentEntity::getId).collect(Collectors.toList());

        //then
        Assertions.assertThat(listOfIds).contains(1L, 2L);
        Assertions.assertThat(result.size()).isEqualTo(2L);
    }

    @Test
    public void findByGradeFAtCertainDayShouldReturnEmptyListIfNoMatches() {
        //Given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy, te);
        StudentEntity ste = saveTestStudent(cy);
        StudentEntity ste1 = saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));

        //When
        List<StudentEntity> result = stRepo.findAllByGradeFAtCertainDay(LocalDate.parse("2022-11-17"));

        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void findByCertainGradeAtCertainDayShouldReturnMatchingStudents() {
        //Given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy, te);
        StudentEntity ste = saveTestStudent(cy);
        StudentEntity ste1 = saveTestStudent(cy);
        StudentEntity ste2 = saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.B, 5, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));
        createGrade(te, ste2, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(2.00));

        //When
        List<StudentEntity> result = stRepo.findAllByCertainGradeAtCertainDay(GradeType.C, LocalDate.parse("2022-11-18"));

        List<Long> listOfIds = result.stream().map(StudentEntity::getId).collect(Collectors.toList());

        //then
        Assertions.assertThat(listOfIds).contains(3L, 2L);
        Assertions.assertThat(result.size()).isEqualTo(2L);
    }

    @Test
    public void findByCertainGradeAtCertainDayShouldReturnEmptyListIfNoMatches() {
        //Given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy, te);
        StudentEntity ste = saveTestStudent(cy);
        StudentEntity ste1 = saveTestStudent(cy);
        StudentEntity ste2 = saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.C, 4, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(2.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-11-15"), BigDecimal.valueOf(4.00));
        createGrade(te, ste1, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.B, 5, LocalDate.parse("2022-11-16"), BigDecimal.valueOf(3.00));
        createGrade(te, ste2, sue, GradeType.C, 4, LocalDate.parse("2022-11-18"), BigDecimal.valueOf(2.00));

        //When
        List<StudentEntity> result = stRepo.findAllByCertainGradeAtCertainDay(GradeType.D, LocalDate.parse("2022-11-18"));

        //then
        Assertions.assertThat(result).isEmpty();
    }


    private void createGrade(TeacherEntity te, StudentEntity ste, SubjectEntity sue, GradeType gt, Integer val, LocalDate date, BigDecimal wg) {
        GradeEntity grade = new GradeEntity();
        grade.setSubjectEntity(sue);
        grade.setStudentEntity(ste);
        grade.setTeacherEntity(te);
        grade.setGradeType(gt);
        grade.setValue(val);
        grade.setDateOfGrade(date);
        grade.setWeight(wg);
        gRepo.save(grade);
    }

}
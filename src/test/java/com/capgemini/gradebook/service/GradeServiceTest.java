package com.capgemini.gradebook.service;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.domain.*;
import com.capgemini.gradebook.exceptions.*;
import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class GradeServiceTest {

    @Inject
    private GradeService gradeService;

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
    public void findGradeByIdShouldReturnProperEntity() {

        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);

        //when
        GradeEto result = gradeService.findGradeById(1L);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void findGradeByIdShouldThrowExceptionIfNotExist() {

        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    gradeService.findGradeById(2L);
                    //then
                }).isInstanceOf(GradeNotFoundException.class)
                .hasMessageContaining("Grade with id: " + 2L + " could not be found");
    }

    @Test
    public void searchGradeByCriteriaShouldReturnGradesIfFound() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.F, 3, LocalDate.parse("2022-12-11"), BigDecimal.valueOf(5.00));
        createGrade(te, ste, sue, GradeType.F, 4, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(4.00));
        createGrade(te, ste, sue, GradeType.D, 5, LocalDate.parse("2022-12-13"), BigDecimal.valueOf(4.00));

        GradeSearchCriteria criteria = new GradeSearchCriteria();
        criteria.setGradeType(GradeType.F);
        criteria.setValueFrom(3);
        criteria.setValueTo(4);
        criteria.setWeightFrom(BigDecimal.valueOf(4.00));
        criteria.setWeightTo(BigDecimal.valueOf(5.00));
        criteria.setCreatedDateFrom(LocalDate.parse("2022-12-11"));
        criteria.setCreatedDateTo(LocalDate.parse("2022-12-12"));
        criteria.setStudentEntityId(1L);
        criteria.setSubjectEntityId(1L);

        List<GradeEto> result = gradeService.searchGradesByCriteria(criteria);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    public void searchGradeByCriteriaShouldReturnEmptyListIfNotFound() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.F, 3, LocalDate.parse("2022-12-11"), BigDecimal.valueOf(5.00));
        createGrade(te, ste, sue, GradeType.F, 4, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(4.00));

        GradeSearchCriteria criteria = new GradeSearchCriteria();
        criteria.setGradeType(GradeType.D);
        criteria.setValueFrom(2);
        criteria.setValueTo(4);
        criteria.setWeightFrom(BigDecimal.valueOf(4.00));
        criteria.setWeightTo(BigDecimal.valueOf(5.00));
        criteria.setCreatedDateFrom(LocalDate.parse("2022-12-11"));
        criteria.setCreatedDateTo(LocalDate.parse("2022-12-13"));
        criteria.setStudentEntityId(1L);
        criteria.setSubjectEntityId(1L);

        List<GradeEto> result = gradeService.searchGradesByCriteria(criteria);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void getWeightedAverageShouldReturnCorrectValue() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-11"), BigDecimal.valueOf(5.00));
        createGrade(te, ste, sue, GradeType.B, 5, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(4.00));
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-12-11"), BigDecimal.valueOf(2.00));

        Double result = gradeService.getWeightedAverage(1L, 1L);

        Assertions.assertThat(result).isEqualTo(2.82);
    }

    @Test
    public void createNewClassYearShouldAlwaysAssignNewId() {

        //Given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestGrade(te,ste,sue);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(5);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));


        //When
        GradeEto result = gradeService.createNew(gradeEto);

        //Then
        Assertions.assertThat(result.getId()).isNotEqualTo(1L);
    }

    @Test
    public void createNewShouldReturnInstanceOfClassYearEto() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(5);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        GradeEto result = gradeService.createNew(gradeEto);

        Assertions.assertThat(result).isInstanceOf(GradeEto.class);
    }

    @Test
    public void createNewShouldReturnClassYearWithMatchingFields() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(5);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        GradeEto result = gradeService.createNew(gradeEto);

        Assertions.assertThat(result.getValue()).isEqualTo(5);
        Assertions.assertThat(result.getDateOfGrade()).isEqualTo(LocalDate.parse("2022-12-12"));
        Assertions.assertThat(result.getStudentEntityId()).isEqualTo(1L);
        Assertions.assertThat(result.getSubjectEntityId()).isEqualTo(1L);
    }

    @Test
    public void createNewWithValue1AndNoCommentShouldThrowException() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(1);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Assertions.assertThatThrownBy(() -> {

            gradeService.createNew(gradeEto);

        }).isInstanceOf(GradeCommentIsEmptyException.class)
                .hasMessageContaining("Comment field for this grade value can't be empty!");
    }

    @Test
    public void createNewWithValue6AndNoCommentShouldThrowException() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(6);
        gradeEto.setComment("");
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Assertions.assertThatThrownBy(() -> {

                    gradeService.createNew(gradeEto);

                }).isInstanceOf(GradeCommentIsEmptyException.class)
                .hasMessageContaining("Comment field for this grade value can't be empty!");
    }

    @Test
    public void createAnotherGradeWithSameGradeTypeAtTheSameDayShouldThrowExeception() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        createGrade(te, ste, sue, GradeType.D, 3, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(4.00));
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(3);
        gradeEto.setGradeType(GradeType.D);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Assertions.assertThatThrownBy(() -> {

                    gradeService.createNew(gradeEto);

                }).isInstanceOf(GradeAlreadyCreatedTodayException.class)
                .hasMessageContaining("Grade of type: " + GradeType.D + " has already been inserted today!");
    }

    @Test
    public void createNewGradeWithValueNotBetween1And6ShouldThrowException() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(7);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Assertions.assertThatThrownBy(() -> {

                    gradeService.createNew(gradeEto);

                }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Error occurred: Grade value must be between 1 and 6");
    }

    @Test
    public void createNewGradeWithWeightNotBetween1And9ShouldThrowException() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(5);
        gradeEto.setWeight(BigDecimal.valueOf(10.00));
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Assertions.assertThatThrownBy(() -> {

                    gradeService.createNew(gradeEto);

                }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Error occurred: Grade weight must be between 1 and 9");
    }

    @Test
    public void createNewShouldThrowExceptionWhenProvidedNotExistingSubjectID() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(2L);
        gradeEto.setStudentEntityId(1L);
        gradeEto.setValue(5);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Assertions.assertThatThrownBy(() -> {
                    //when
                    gradeService.createNew(gradeEto);
                    //then
                }).isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with id: " + 2L + " could not be found");
    }

    @Test
    public void createNewShouldThrowExceptionWhenProvidedNotExistingClassYearID() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        tec.saveTestStudent(cy);
        GradeEto gradeEto = new GradeEto();
        gradeEto.setId(1L);
        gradeEto.setSubjectEntityId(1L);
        gradeEto.setStudentEntityId(2L);
        gradeEto.setValue(5);
        gradeEto.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Assertions.assertThatThrownBy(() -> {
                    //when
                    gradeService.createNew(gradeEto);
                    //then
                }).isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id: " + 2L + " could not be found");
    }

    @Test
    public void partialUpdateShouldReturnInstanceOfGradeEto() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);

        Map<String, Object> info = new HashMap<>();
        info.put("value", 3);
        info.put("studentEntityId", 2);

        GradeEto gradeEto = gradeService.partialUpdate(1L, info);

        Assertions.assertThat(gradeEto).isInstanceOf(GradeEto.class);
    }

    @Test
    public void partialUpdateShouldReturnStudentWithNewValues() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);

        Integer oldValue = this.grepo.findById(1L).get().getValue();
        Long oldStudentEntityId = this.grepo.findById(1L).get().getStudentEntity().getId();

        Map<String, Object> info = new HashMap<>();
        info.put("value", 3);
        info.put("studentEntityId", 2);

        GradeEto gradeEto = gradeService.partialUpdate(1L, info);

        Integer newValue = this.grepo.findById(1L).get().getValue();
        Long newStudentEntityId = this.grepo.findById(1L).get().getStudentEntity().getId();


        Assertions.assertThat(oldValue).isNotEqualTo(newValue);
        Assertions.assertThat(oldStudentEntityId).isNotEqualTo(newStudentEntityId);
        Assertions.assertThat(newValue).isEqualTo(3);
        Assertions.assertThat(newStudentEntityId).isEqualTo(2L);
    }

    @Test
    public void partialUpdateWithNotExistingStudentShouldThrowException() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);

        Map<String, Object> info = new HashMap<>();
        info.put("value", 3);
        info.put("studentEntityId", 2);

        Assertions.assertThatThrownBy(() -> {

                    gradeService.partialUpdate(1L, info);

                }).isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id: " + 2L + " could not be found");
    }

    @Test
    public void partialUpdateWithNotExistingSubjectShouldThrowException() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);

        Map<String, Object> info = new HashMap<>();
        info.put("value", 3);
        info.put("subjectEntityId", 2);

        Assertions.assertThatThrownBy(() -> {

                    gradeService.partialUpdate(1L, info);

                }).isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with id: " + 2L + " could not be found");
    }

    @Test
    public void deleteGradeAndThenGetItShouldThrowException() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);


        Assertions.assertThatThrownBy(() -> {

                    gradeService.delete(1L);

                    GradeEto result = gradeService.findGradeById(1L);

                }).isInstanceOf(GradeNotFoundException.class)
                .hasMessageContaining("Grade with id: " + 1L + " could not be found");
    }

    @Test
    public void deleteGradeShouldLeaveEmptyDatabaseTable() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        StudentEntity ste = tec.saveTestStudent(cy);
        tec.saveTestGrade(te, ste, sue);

        gradeService.delete(1L);

        List<Grade> grades = this.grepo.findAll();

        Assertions.assertThat(grades).isEmpty();
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



package com.capgemini.gradebook.service;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.domain.SubjectEto;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.exceptions.SubjectNotFoundException;
import com.capgemini.gradebook.exceptions.TeacherNotFoundException;
import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import com.capgemini.gradebook.persistence.repo.SubjectRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import java.util.List;


@SpringBootTest
class SubjectServiceTest {

    @Inject
    private SubjectService subjectService;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private TestEntityCreator tec;

    @Inject
    private SubjectRepo surepo;

    @Inject
    private GradeRepo grepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }

    @Test
    public void findSubjectByIdShouldReturnProperEntity() {

        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);

        //when
        SubjectEto result = subjectService.findSubjectById(1L);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void findSubjectByIdShouldThrowExceptionIfNotExist() {

        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    SubjectEto result = subjectService.findSubjectById(2L);
                    //then
                }).isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with id: " + 2L + " could not be found");
    }

    @Test
    public void createNewSubjectShouldAlwaysAssignNewId() {

        //Given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setId(1L);
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setClassYearId(1L);
        subjectEto.setSubjectType(SubjectType.CHEMISTRY);

        //When
        SubjectEto result = subjectService.createNew(subjectEto);

        //Then
        Assertions.assertThat(result.getId()).isNotEqualTo(1L);
    }

    @Test
    public void createNewShouldReturnInstanceOfSubjectEto() {
        //given
        tec.saveTestTeacher();
        tec.saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setClassYearId(1L);
        subjectEto.setSubjectType(SubjectType.CHEMISTRY);
        //when
        SubjectEto result = subjectService.createNew(subjectEto);
        //then
        Assertions.assertThat(result).isInstanceOf(SubjectEto.class);
    }

    @Test
    public void createNewShouldReturnSubjectWithMatchingFields() {
        //given
        tec.saveTestTeacher();
        tec.saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setClassYearId(1L);
        subjectEto.setSubjectType(SubjectType.CHEMISTRY);
        //when
        SubjectEto result = subjectService.createNew(subjectEto);
        //then
        Assertions.assertThat(result.getSubjectType()).isEqualTo(SubjectType.CHEMISTRY);
        Assertions.assertThat(result.getTeacherEntityId()).isEqualTo(1L);
    }

    @Test
    public void createNewShouldThrowExceptionWhenProvidedNotExistingTeacherID() {
        //given
        tec.saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setClassYearId(1L);
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setSubjectType(SubjectType.BIOLOGY);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    this.subjectService.createNew(subjectEto);
                    //then
                }).isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with id: " + 1L+ " could not be found");
    }

    @Test
    public void createNewShouldThrowExceptionWhenProvidedNotExistingClassYearID() {
        //given
        tec.saveTestTeacher();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setClassYearId(1L);
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setSubjectType(SubjectType.BIOLOGY);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    this.subjectService.createNew(subjectEto);
                    //then
                }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + 1L+ " could not be found");
    }

    @Test
    public void createNewShouldThrowExceptionWhenNoClassYearIdProvided() {
        //given
        tec.saveTestTeacher();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setSubjectType(SubjectType.BIOLOGY);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    subjectService.createNew(subjectEto);
                    //then
                }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void createNewShouldThrowExceptionWhenNoTeacherEntityIdProvided() {
        //given
        tec.saveTestTeacher();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setClassYearId(1L);
        subjectEto.setSubjectType(SubjectType.BIOLOGY);

        Assertions.assertThatThrownBy(() -> {
            //when
            subjectService.createNew(subjectEto);
            //then
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void createNewShouldThrowExceptionWhenNoSubjectTypeProvided() {
        //given
        tec.saveTestTeacher();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setClassYearId(1L);

        Assertions.assertThatThrownBy(() -> {
            //when
            subjectService.createNew(subjectEto);
            //then
        }).isInstanceOf(ConstraintViolationException.class);
    }



    @Test
    public void updateSubjectTeacherShouldReturnSubjectWithNewTeacherID() {
        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);
        tec.saveTestTeacher();

        Long oldID = surepo.findById(1L).get().getTeacherEntity().getId();
        //when
        SubjectEto updatedSubject = subjectService.updateSubjectTeacher(1L, 2L);

        Long newID = surepo.findById(1L).get().getTeacherEntity().getId();
        //then
        Assertions.assertThat(newID).isNotEqualTo(oldID);
        Assertions.assertThat(updatedSubject.getTeacherEntityId()).isEqualTo(newID);
    }

    @Test
    public void updateSubjectTeacherShouldThrowExceptionWhenProvideNotExistingTeacherID() {
        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    this.subjectService.updateSubjectTeacher(1L, 2L);
                    //then
                }).isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with id: " + 2L+ " could not be found");
    }


    @Test
    public void deleteSubjectAndThenGetItShouldThrowException() {
        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    subjectService.delete(1L);
                    SubjectEto result = subjectService.findSubjectById(1L);
                    //then
                }).isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with id: " + 1L + " could not be found");
    }

    @Test
    public void deleteSubjectShouldLeaveEmptyDatabaseTable() {
        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);
        //when
        subjectService.delete(1L);
        List<SubjectEntity> subjects = this.surepo.findAll();
        //then
        Assertions.assertThat(subjects).isEmpty();
    }

    @Test
    public void deleteSubjectShouldDeleteAllAssignedGrades() {
        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        StudentEntity ste = tec.saveTestStudent(cy);
        SubjectEntity sue = tec.saveTestSubject(cy,te);
        tec.saveTestGrade(te,ste,sue);
        tec.saveTestGrade(te,ste,sue);

        Integer oldGradesCount = this.grepo.findAllBySubjectEntityId(1L).size();
        //when
        subjectService.delete(1L);

        Integer newGradesCount = this.grepo.findAllBySubjectEntityId(1L).size();
        //then
        Assertions.assertThat(oldGradesCount).isNotEqualTo(newGradesCount);
        Assertions.assertThat(newGradesCount).isEqualTo(0);

    }


    }

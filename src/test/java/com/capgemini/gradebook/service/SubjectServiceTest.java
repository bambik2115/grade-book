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

        tec.saveTestTeacher();
        tec.saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setClassYearId(1L);
        subjectEto.setSubjectType(SubjectType.CHEMISTRY);

        SubjectEto result = subjectService.createNew(subjectEto);

        Assertions.assertThat(result).isInstanceOf(SubjectEto.class);
    }

    @Test
    public void createNewShouldReturnSubjectWithMatchingFields() {

        tec.saveTestTeacher();
        tec.saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(1L);
        subjectEto.setClassYearId(1L);
        subjectEto.setSubjectType(SubjectType.CHEMISTRY);

        SubjectEto result = subjectService.createNew(subjectEto);

        Assertions.assertThat(result.getSubjectType()).isEqualTo(SubjectType.CHEMISTRY);
        Assertions.assertThat(result.getTeacherEntityId()).isEqualTo(1L);
    }

    @Test
    public void createNewShouldThrowExceptionWhenProvidedNotExistingTeacherID() {

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
    public void updateSubjectTeacherShouldReturnSubjectWithNewTeacherID() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);
        tec.saveTestTeacher();

        Long oldID = surepo.findById(1L).get().getTeacherEntity().getId();

        SubjectEto updatedSubject = subjectService.updateSubjectTeacher(1L, 2L);

        Long newID = surepo.findById(1L).get().getTeacherEntity().getId();

        Assertions.assertThat(newID).isNotEqualTo(oldID);
        Assertions.assertThat(updatedSubject.getTeacherEntityId()).isEqualTo(newID);
    }

    @Test
    public void updateSubjectTeacherShouldThrowExceptionWhenProvideNotExistingTeacherID() {

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
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);

        Assertions.assertThatThrownBy(() -> {
                    subjectService.delete(1L);
                    SubjectEto result = subjectService.findSubjectById(1L);
                }).isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with id: " + 1L + " could not be found");
    }

    @Test
    public void deleteSubjectShouldLeaveEmptyDatabaseTable() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestSubject(cy,te);

        subjectService.delete(1L);
        List<SubjectEntity> subjects = this.surepo.findAll();

        Assertions.assertThat(subjects).isEmpty();
    }

    @Test
    public void deleteSubjectShouldDeleteAllAssignedGrades() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        StudentEntity ste = tec.saveTestStudent(cy);
        SubjectEntity sue = tec.saveTestSubject(cy,te);
        tec.saveTestGrade(te,ste,sue);
        tec.saveTestGrade(te,ste,sue);

        Integer oldGradesCount = this.grepo.findAllBySubjectEntityId(1L).size();
        //when
        subjectService.delete(1L);

        //then
        Integer newGradesCount = this.grepo.findAllBySubjectEntityId(1L).size();

        Assertions.assertThat(oldGradesCount).isNotEqualTo(newGradesCount);
        Assertions.assertThat(newGradesCount).isEqualTo(0);

    }


    }

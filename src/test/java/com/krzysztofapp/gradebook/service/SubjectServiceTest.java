package com.krzysztofapp.gradebook.service;

import com.krzysztofapp.gradebook.DbCleanUpService;
import com.krzysztofapp.gradebook.TestEntityCreator;
import com.krzysztofapp.gradebook.domain.SubjectEto;
import com.krzysztofapp.gradebook.exceptions.ClassYearNotFoundException;
import com.krzysztofapp.gradebook.exceptions.SubjectNotFoundException;
import com.krzysztofapp.gradebook.exceptions.TeacherNotFoundException;
import com.krzysztofapp.gradebook.persistence.repo.GradeRepo;
import com.krzysztofapp.gradebook.persistence.repo.SubjectRepo;
import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;
import com.krzysztofapp.gradebook.persistence.entity.StudentEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectType;
import com.krzysztofapp.gradebook.persistence.entity.TeacherEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import java.util.List;


@SpringBootTest
class SubjectServiceTest extends TestEntityCreator {

    @Inject
    private SubjectService subjectService;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private SubjectRepo suRepo;

    @Inject
    private GradeRepo gRepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }

    @Test
    public void findSubjectByIdShouldReturnProperEntity() {

        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy,te);

        //when
        SubjectEto result = subjectService.findSubjectById(sue.getId());

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(sue.getId());
    }

    @Test
    public void findSubjectByIdShouldThrowExceptionIfNotExist() {

        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy,te);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    SubjectEto result = subjectService.findSubjectById(sue.getId()+1);
                    //then
                }).isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with id: " + (sue.getId()+1) + " could not be found");
    }


    @Test
    public void createNewShouldReturnSubjectWithMatchingFields() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cye = saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(te.getId());
        subjectEto.setClassYearEntityId(cye.getId());
        subjectEto.setSubjectType(SubjectType.CHEMISTRY);
        //when
        SubjectEto result = subjectService.createNew(subjectEto);
        //then
        Assertions.assertThat(result.getSubjectType()).isEqualTo(SubjectType.CHEMISTRY);
        Assertions.assertThat(result.getTeacherEntityId()).isEqualTo(te.getId());
    }

    @Test
    public void createNewShouldThrowExceptionWhenProvidedNotExistingTeacherID() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cye = saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setClassYearEntityId(cye.getId());
        subjectEto.setTeacherEntityId(te.getId()+1);
        subjectEto.setSubjectType(SubjectType.BIOLOGY);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    subjectService.createNew(subjectEto);
                    //then
                }).isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with id: " + (te.getId()+1) + " could not be found");
    }

    @Test
    public void createNewShouldThrowExceptionWhenProvidedNotExistingClassYearID() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cye = saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setClassYearEntityId(cye.getId()+1);
        subjectEto.setTeacherEntityId(te.getId());
        subjectEto.setSubjectType(SubjectType.BIOLOGY);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    subjectService.createNew(subjectEto);
                    //then
                }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + (cye.getId()+1) + " could not be found");
    }

    @Test
    public void createNewShouldThrowExceptionWhenNoClassYearIdProvided() {
        //given
        TeacherEntity te = saveTestTeacher();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(te.getId());
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
        ClassYearEntity cye = saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setClassYearEntityId(cye.getId());
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
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cye = saveTestClassYear();
        SubjectEto subjectEto = new SubjectEto();
        subjectEto.setTeacherEntityId(te.getId());
        subjectEto.setClassYearEntityId(cye.getId());

        Assertions.assertThatThrownBy(() -> {
            //when
            subjectService.createNew(subjectEto);
            //then
        }).isInstanceOf(ConstraintViolationException.class);
    }



    @Test
    public void updateSubjectTeacherShouldReturnSubjectWithNewTeacherID() {
        //given
        TeacherEntity te = saveTestTeacher();
        TeacherEntity te1 = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy,te);

        //when
        SubjectEto updatedSubject = subjectService.updateSubjectTeacher(sue.getId(), te1.getId());

        Long newID = suRepo.findById(sue.getId()).get().getTeacherEntity().getId();
        //then
        Assertions.assertThat(updatedSubject.getTeacherEntityId()).isEqualTo(newID);
    }

    @Test
    public void updateSubjectTeacherShouldThrowExceptionWhenProvideNotExistingTeacherID() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy,te);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    subjectService.updateSubjectTeacher(sue.getId(), te.getId()+1);
                    //then
                }).isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with id: " + (te.getId()+1) + " could not be found");
    }


    @Test
    public void findingSubjectAfterDeleteShouldThrowException() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy,te);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    subjectService.delete(sue.getId());
                    SubjectEto result = subjectService.findSubjectById(sue.getId());
                    //then
                }).isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with id: " + sue.getId() + " could not be found");
    }

    @Test
    public void deleteSubjectShouldLeaveEmptyDatabaseTable() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cy,te);
        //when
        subjectService.delete(sue.getId());
        List<SubjectEntity> subjects = suRepo.findAll();
        //then
        Assertions.assertThat(subjects).isEmpty();
    }

    @Test
    public void deleteSubjectShouldDeleteAllAssignedGrades() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);
        SubjectEntity sue = saveTestSubject(cy,te);
        saveTestGrade(te,ste,sue);
        saveTestGrade(te,ste,sue);

        Integer oldGradesCount = gRepo.findAllBySubjectEntityId(sue.getId()).size();
        //when
        subjectService.delete(sue.getId());

        Integer newGradesCount = gRepo.findAllBySubjectEntityId(sue.getId()).size();
        //then
        Assertions.assertThat(oldGradesCount).isNotEqualTo(newGradesCount);
        Assertions.assertThat(newGradesCount).isEqualTo(0);

    }


    }

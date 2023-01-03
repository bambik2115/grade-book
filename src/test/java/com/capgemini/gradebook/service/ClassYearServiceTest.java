package com.capgemini.gradebook.service;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.domain.ClassYearEto;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.persistence.entity.ClassYear;
import com.capgemini.gradebook.persistence.entity.SubjectEntity;
import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.ClassYearRepo;
import com.capgemini.gradebook.persistence.repo.SubjectRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ClassYearServiceTest {


    @Inject
    private ClassYearService classYearService;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private TestEntityCreator tec;

    @Inject
    private SubjectRepo surepo;

    @Inject
    private ClassYearRepo cyrepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }


    @Test
    public void findClassYearByIdShouldThrowExceptionIfNotExist() {

        //given
        tec.saveTestClassYear();

        Assertions.assertThatThrownBy(() -> {
                    //when
                    ClassYearEto result = classYearService.findClassYearById(2L);
                    //then
                }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + 2L + " could not be found");
    }

    @Test
    public void findClassYearByIdShouldReturnProperEntity() {

        //given
        tec.saveTestClassYear();

        //when
        ClassYearEto result = classYearService.findClassYearById(1L);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void createNewClassYearShouldAlwaysAssignNewId() {

        //Given
        tec.saveTestClassYear();
        ClassYearEto classYearEto = new ClassYearEto();
        classYearEto.setId(1L);
        classYearEto.setClassYear("2022");
        classYearEto.setClassLevel(1);
        classYearEto.setClassName("D");

        //When
        ClassYearEto result = classYearService.createNew(classYearEto);

        //Then
        Assertions.assertThat(result.getId()).isNotEqualTo(1L);
    }

    @Test
    public void createNewShouldReturnInstanceOfClassYearEto() {

        //given
        ClassYearEto classYearEto = new ClassYearEto();
        classYearEto.setClassName("D");
        classYearEto.setClassYear("2022");
        classYearEto.setClassLevel(1);

        //when
        ClassYearEto result = classYearService.createNew(classYearEto);

        //then
        Assertions.assertThat(result).isInstanceOf(ClassYearEto.class);
    }

    @Test
    public void createNewShouldReturnClassYearWithMatchingFields() {
        //given
        ClassYearEto classYearEto = new ClassYearEto();
        classYearEto.setClassName("D");
        classYearEto.setClassYear("2022");
        classYearEto.setClassLevel(1);

        //when
        ClassYearEto result = classYearService.createNew(classYearEto);

        //then
        Assertions.assertThat(result.getClassYear()).isEqualTo("2022");
        Assertions.assertThat(result.getClassName()).isEqualTo("D");
        Assertions.assertThat(result.getClassLevel()).isEqualTo(1);
    }

    @Test
    public void createNewShouldThrowExceptionWhenClassYearNotProvided() {
        //given
        ClassYearEto classYearEto = new ClassYearEto();
        classYearEto.setClassName("D");
        classYearEto.setClassLevel(1);

        Assertions.assertThatThrownBy(() -> {

            //when
            classYearService.createNew(classYearEto);

            //then
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void createNewShouldThrowExceptionWhenClassNameNotProvided() {
        //given
        ClassYearEto classYearEto = new ClassYearEto();
        classYearEto.setClassYear("2022");
        classYearEto.setClassLevel(1);

        Assertions.assertThatThrownBy(() -> {

            //when
            classYearService.createNew(classYearEto);

            //then
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void createNewShouldThrowExceptionWhenClassLevelNotProvided() {
        //given
        ClassYearEto classYearEto = new ClassYearEto();
        classYearEto.setClassYear("2022");
        classYearEto.setClassName("D");

        Assertions.assertThatThrownBy(() -> {
            //when
            classYearService.createNew(classYearEto);

            //then
        }).isInstanceOf(ConstraintViolationException.class);
    }



    @Test
    public void partialUpdateShouldReturnInstanceOfClassYearEto() {
        //given
        tec.saveTestClassYear();
        Map<String, Object> info = new HashMap<>();
        info.put("className", "C");

        //when
        ClassYearEto result = classYearService.partialUpdate(1L, info);

        //then
        Assertions.assertThat(result).isInstanceOf(ClassYearEto.class);

    }

    @Test
    public void partialUpdateShouldReturnClassYearWithNewValues() {
        //Given
        ClassYear cy = tec.saveTestClassYear();
        Map<String, Object> info = new HashMap<>();
        info.put("classLevel", 3);
        info.put("className", "C");

        //When
        ClassYearEto result = classYearService.partialUpdate(1L, info);

        //Then
        Assertions.assertThat(result.getClassName()).isNotEqualTo(cy.getClassName());
        Assertions.assertThat(result.getClassLevel()).isNotEqualTo(cy.getClassLevel());
        Assertions.assertThat(result.getClassName()).isEqualTo("C");
        Assertions.assertThat(result.getClassLevel()).isEqualTo(3);
    }

    @Test
    public void partialUpdateWithNewClassNameOrLevelShouldUpdateSubjectName() {
        //given
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        SubjectEntity sue = tec.saveTestSubject(cy, te);
        Map<String, Object> info = new HashMap<>();
        info.put("className", "E");
        info.put("classLevel", 2);

        String oldName = surepo.findById(1L).get().getName();

        //When
        ClassYearEto classYearEto = classYearService.partialUpdate(1L, info);

        String newName = surepo.findById(1L).get().getName();

        //Then
        Assertions.assertThat(newName).isNotEqualTo(oldName);
        Assertions.assertThat(newName).isEqualTo("BIOLOGY_2E");
    }


    @Test
    public void deleteClassYearAndThenGetItShouldThrowException() {
        //given
        tec.saveTestClassYear();

        Assertions.assertThatThrownBy(() -> {

                    //When
                    classYearService.delete(1L);
                    ClassYearEto result = classYearService.findClassYearById(1L);

                    //Then
                }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + 1L + " could not be found");

    }


    @Test
    public void deleteClassYearShouldLeaveEmptyDatabaseTable() {
        //given
        tec.saveTestClassYear();

        //When
        classYearService.delete(1L);
        List<ClassYear> classyears = this.cyrepo.findAll();

        //Then
        Assertions.assertThat(classyears).isEmpty();
    }


}
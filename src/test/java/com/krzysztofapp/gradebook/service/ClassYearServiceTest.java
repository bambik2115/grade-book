package com.krzysztofapp.gradebook.service;

import com.krzysztofapp.gradebook.DbCleanUpService;
import com.krzysztofapp.gradebook.TestEntityCreator;
import com.krzysztofapp.gradebook.domain.ClassYearEto;
import com.krzysztofapp.gradebook.exceptions.ClassYearNotFoundException;
import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectEntity;
import com.krzysztofapp.gradebook.persistence.entity.TeacherEntity;
import com.krzysztofapp.gradebook.persistence.repo.ClassYearRepo;
import com.krzysztofapp.gradebook.persistence.repo.SubjectRepo;
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
class ClassYearServiceTest extends TestEntityCreator {


    @Inject
    private ClassYearService classYearService;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private SubjectRepo suRepo;

    @Inject
    private ClassYearRepo cyRepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }


    @Test
    public void findClassYearByIdShouldThrowExceptionIfNotExist() {

        //given
        saveTestClassYear();

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
        ClassYearEntity cye = saveTestClassYear();


        //when
        ClassYearEto result = classYearService.findClassYearById(1L);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(cye.getId());
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
        ClassYearEntity cye = saveTestClassYear();
        Map<String, Object> info = new HashMap<>();
        info.put("className", "C");

        //when
        ClassYearEto result = classYearService.partialUpdate(cye.getId(), info);

        //then
        Assertions.assertThat(result).isInstanceOf(ClassYearEto.class);

    }

    @Test
    public void partialUpdateShouldReturnClassYearWithNewValues() {
        //Given
        ClassYearEntity cye = saveTestClassYear();
        Map<String, Object> info = new HashMap<>();
        info.put("classLevel", 3);
        info.put("className", "C");

        //When
        ClassYearEto result = classYearService.partialUpdate(cye.getId(), info);

        //Then
        Assertions.assertThat(result.getClassName()).isNotEqualTo(cye.getClassName());
        Assertions.assertThat(result.getClassLevel()).isNotEqualTo(cye.getClassLevel());
        Assertions.assertThat(result.getClassName()).isEqualTo("C");
        Assertions.assertThat(result.getClassLevel()).isEqualTo(3);
    }

    @Test
    public void partialUpdateWithNewClassNameAndLevelShouldUpdateSubjectName() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cye = saveTestClassYear();
        SubjectEntity sue = saveTestSubject(cye, te);
        Map<String, Object> info = new HashMap<>();
        info.put("className", "E");
        info.put("classLevel", 2);

        //When
        ClassYearEto classYearEto = classYearService.partialUpdate(cye.getId(), info);

        String newName = suRepo.findById(cye.getId()).get().getName();

        //Then
        Assertions.assertThat(newName).isEqualTo("BIOLOGY_2E");
    }


    @Test
    public void findingClassYearAfterDeleteShouldThrowException() {
        //given
        ClassYearEntity cye = saveTestClassYear();


        Assertions.assertThatThrownBy(() -> {

                    //When
                    classYearService.delete(cye.getId());
                    ClassYearEto result = classYearService.findClassYearById(cye.getId());

                    //Then
                }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + cye.getId() + " could not be found");

    }


    @Test
    public void deleteClassYearShouldLeaveEmptyDatabaseTable() {
        //given
        ClassYearEntity cye = saveTestClassYear();

        //When
        classYearService.delete(cye.getId());
        List<ClassYearEntity> classYears = cyRepo.findAll();

        //Then
        Assertions.assertThat(classYears).isEmpty();
    }


}
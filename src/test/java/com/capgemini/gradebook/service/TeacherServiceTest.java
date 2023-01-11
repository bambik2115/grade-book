package com.capgemini.gradebook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.exceptions.TeacherNotFoundException;
import com.capgemini.gradebook.exceptions.TeacherStillInUseException;
import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.repo.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.capgemini.gradebook.domain.TeacherEto;

@SpringBootTest
class TeacherServiceTest {


  @Inject
  private TestEntityCreator tec;

  @Inject
  private TeacherService teacherService;

  @Inject
  private TeacherRepo tRepo;

  @Inject
  private SubjectRepo suRepo;

  @Inject
  private GradeRepo gRepo;

  @Inject
  private DbCleanUpService testDbService;


  @AfterEach
  private void cleanDbBetweenTests() {
    testDbService.resetDatabase();
  }

  /**
   * Integration test that persists some test data and checks, if after findAllTeachers the populated list will not
   * be empty
   */
  @Test
  public void findAllTeachersShouldNotBeEmptyAfterInsert(){

    // given
    tec.saveTestTeacher();
    // when
    List<TeacherEto> result = teacherService.findAllTeachers();
    // then
    Assertions.assertThat(result).isNotEmpty();
  }

  //TODO IMPLEMENT: Write other tests, that will test if: save, update, findOne, delete also work properly

  @Test
  public void findAllTeachersShouldReturnEmptyListIfNoEntries() {

    List<TeacherEto> result = teacherService.findAllTeachers();

    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void findByNameShouldBeEmptyIfNotExist(){

    //given
    tec.saveTestTeacher();

    //when
    List<TeacherEto> result = teacherService.findTeachersByLastName("Kowarski");

    //then
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void findTeacherByIdShouldThrowExceptionIfNotExist() {

    //given
    tec.saveTestTeacher();

    Assertions.assertThatThrownBy(() -> {
                      //when
                      TeacherEto result = teacherService.findTeacherById(2L);
                      //then
                    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + 2L + " could not be found");
  }

  @Test
  public void findTeacherByIdShouldReturnProperEntity() {

    //given
    tec.saveTestTeacher();

    //when
    TeacherEto result = teacherService.findTeacherById(1L);

    //then
    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getId()).isEqualTo(1L);
  }

  @Test
  public void createNewUserShouldAlwaysAssignNewId() {

    //Given
    tec.saveTestTeacher();
    TeacherEto teacherEto = new TeacherEto();
    teacherEto.setId(1L);
    teacherEto.setFirstName("Kamil");
    teacherEto.setLastName("Komar");

    //When
    TeacherEto result = teacherService.createNew(teacherEto);

    //Then
    Assertions.assertThat(result.getId()).isNotEqualTo(1L);
  }

  @Test
  public void createNewShouldReturnInstanceOfTeacherEto() {

    TeacherEto teacherEto = new TeacherEto();
    //when
    TeacherEto result = teacherService.createNew(teacherEto);
    //then
    Assertions.assertThat(result).isInstanceOf(TeacherEto.class);
  }

  @Test
  public void createNewShouldReturnTeacherWithMatchingFields() {

    TeacherEto teacherEto = new TeacherEto();
    teacherEto.setFirstName("Kamil");
    teacherEto.setLastName("Komar");
    //when
    TeacherEto result = teacherService.createNew(teacherEto);
    //then
    Assertions.assertThat(result.getFirstName()).isEqualTo("Kamil");
    Assertions.assertThat(result.getLastName()).isEqualTo("Komar");
  }

  @Test
  public void createNewShouldThrowExceptionWhenNoFirstNameProvided() {

    TeacherEto teacherEto = new TeacherEto();
    teacherEto.setLastName("Komar");


    Assertions.assertThatThrownBy(() -> {
      //when
      teacherService.createNew(teacherEto);
      //then
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  public void createNewShouldThrowExceptionWhenNoLastNameProvided() {

    TeacherEto teacherEto = new TeacherEto();
    teacherEto.setFirstName("Kamil");


    Assertions.assertThatThrownBy(() -> {
      //when
      teacherService.createNew(teacherEto);
      //then
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  public void partialUpdateShouldReturnInstanceOfTeacherEto() {

    tec.saveTestTeacher();
    Map<String, Object> info = new HashMap<>();
    info.put("firstName", "Kuba");
    //when
    TeacherEto teacherEto = teacherService.partialUpdate(1L, info);
    //then
    Assertions.assertThat(teacherEto).isInstanceOf(TeacherEto.class);

  }

  @Test
  public void partialUpdateShouldReturnTeacherWithNewValues() {
    //Given
    TeacherEto oldteacher = new TeacherEto();
    oldteacher.setFirstName("Kamil");
    oldteacher.setLastName("Korek");
    teacherService.createNew(oldteacher);

    String oldName = tRepo.findById(1L).get().getFirstName();

    Map<String, Object> info = new HashMap<>();
    info.put("firstName", "Kuba");

    //When
    TeacherEto teacherEto = teacherService.partialUpdate(1L, info);

    String newName = tRepo.findById(1L).get().getFirstName();

    //Then
    Assertions.assertThat(oldName).isNotEqualTo(newName);
    Assertions.assertThat(newName).isEqualTo("Kuba");
  }

  @Test
  public void deleteTeacherWithNewIdShouldLeaveNoNullSubjectTeacherIDs () {
    //Given
    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    tRepo.save(te2);
    ClassYearEntity cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);
    //when
    teacherService.delete(1L, Optional.of(2L));

    List<SubjectEntity> result = suRepo.findAllByTeacherEntityIdIsNull();
    //then
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void deleteTeacherWithNewIdShouldLeaveNoNullGradeTeacherIDs() {
    //Given
    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    tRepo.save(te2);
    ClassYearEntity cy = tec.saveTestClassYear();
    SubjectEntity su = tec.saveTestSubject(cy, te);
    StudentEntity st = tec.saveTestStudent(cy);
    tec.saveTestGrade(te, st, su);

    //when
    teacherService.delete(1L, Optional.of(2L));

    List<GradeEntity> result = gRepo.findAllByTeacherEntityIdIsNull();
    //then
    Assertions.assertThat(result).isEmpty();

  }

  @Test
  public void deleteTeacherWithNewIdShouldAssignNewTeacherIdToSubject() {
    //Given
    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    tRepo.save(te2);
    ClassYearEntity cy = tec.saveTestClassYear();
    SubjectEntity se = tec.saveTestSubject(cy, te);

    Long oldId = suRepo.findById(1L).get().getTeacherEntity().getId();

    //when
    teacherService.delete(1L, Optional.of(2L));

    Long newId = suRepo.findById(1L).get().getTeacherEntity().getId();

    //then
    Assertions.assertThat(newId).isNotEqualTo(oldId);
    Assertions.assertThat(newId).isEqualTo(2L);
  }

  @Test
  public void deleteTeacherWithNewIdShouldAssignNewTeacherIdToGrade() {
    //Given
    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    tRepo.save(te2);
    ClassYearEntity cy = tec.saveTestClassYear();
    SubjectEntity sue = tec.saveTestSubject(cy, te);
    StudentEntity ste = tec.saveTestStudent(cy);
    tec.saveTestGrade(te, ste, sue);

    Long oldId = gRepo.findById(1L).get().getTeacherEntity().getId();

    //when
    teacherService.delete(1L, Optional.of(2L));

    Long newId = gRepo.findById(1L).get().getTeacherEntity().getId();
    //then
    Assertions.assertThat(newId).isNotEqualTo(oldId);
    Assertions.assertThat(newId).isEqualTo(2L);
  }

  @Test
  public void deleteTeacherWithNotExistingNewIdShouldThrowException() {
    //Given
    TeacherEntity te = tec.saveTestTeacher();
    ClassYearEntity cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);

    Assertions.assertThatThrownBy(() -> {

              //when
      teacherService.delete(1L, Optional.of(2L));
              //then
    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + 2L + " could not be found");
  }

  @Test
  public void deleteTeacherAndThenGetItShouldThrowException() {
    //Given
    tec.saveTestTeacher();

    Assertions.assertThatThrownBy(() -> {

              //when
      teacherService.delete(1L, Optional.empty());

      TeacherEto result = teacherService.findTeacherById(1L);
              //then
    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + 1L + " could not be found");

  }

  @Test
  public void deleteTeacherInUseWithoutNewIdShouldThrowException() {
    //Given
    TeacherEntity te = tec.saveTestTeacher();
    ClassYearEntity cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);

    Assertions.assertThatThrownBy(() -> {
              //when
      teacherService.delete(1L, Optional.empty());
              //then
    }).isInstanceOf(TeacherStillInUseException.class)
            .hasMessageContaining("Teacher with ID: " + 1L + " is in use, please pass ID to update");
  }

  @Test
  public void deleteTeacherShouldLeaveEmptyDatabaseTable() {
    //Given
    tec.saveTestTeacher();

    //when
    teacherService.delete(1L, Optional.empty());

    List<TeacherEntity> teachers = tRepo.findAll();
    //then
    Assertions.assertThat(teachers).isEmpty();
  }

  }

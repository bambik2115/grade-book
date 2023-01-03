package com.capgemini.gradebook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.domain.SubjectEto;
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
  private TeacherRepo trepo;

  @Inject
  private SubjectRepo surepo;

  @Inject
  private GradeRepo grepo;
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
    TeacherEto teachereto = new TeacherEto();
    teachereto.setId(1L);
    teachereto.setFirstName("Kamil");
    teachereto.setLastName("Komar");

    //When
    TeacherEto result = teacherService.createNew(teachereto);

    //Then
    Assertions.assertThat(result.getId()).isNotEqualTo(1L);
  }

  @Test
  public void createNewShouldReturnInstanceOfTeacherEto() {

    TeacherEto teachereto = new TeacherEto();
    //when
    TeacherEto result = teacherService.createNew(teachereto);
    //then
    Assertions.assertThat(result).isInstanceOf(TeacherEto.class);
  }

  @Test
  public void createNewShouldReturnTeacherWithMatchingFields() {

    TeacherEto teachereto = new TeacherEto();
    teachereto.setFirstName("Kamil");
    teachereto.setLastName("Komar");
    //when
    TeacherEto result = teacherService.createNew(teachereto);
    //then
    Assertions.assertThat(result.getFirstName()).isEqualTo("Kamil");
    Assertions.assertThat(result.getLastName()).isEqualTo("Komar");
  }

  @Test
  public void createNewShouldThrowExceptionWhenNoFirstNameProvided() {

    TeacherEto teachereto = new TeacherEto();
    teachereto.setLastName("Komar");


    Assertions.assertThatThrownBy(() -> {
      //when
      teacherService.createNew(teachereto);
      //then
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  public void createNewShouldThrowExceptionWhenNoLastNameProvided() {

    TeacherEto teachereto = new TeacherEto();
    teachereto.setFirstName("Kamil");


    Assertions.assertThatThrownBy(() -> {
      //when
      teacherService.createNew(teachereto);
      //then
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  public void partialUpdateShouldReturnInstanceOfTeacherEto() {

    tec.saveTestTeacher();
    Map<String, Object> info = new HashMap<>();
    info.put("firstName", "Kuba");
    //when
    TeacherEto teachereto = teacherService.partialUpdate(1L, info);
    //then
    Assertions.assertThat(teachereto).isInstanceOf(TeacherEto.class);

  }

  @Test
  public void partialUpdateShouldReturnTeacherWithNewValues() {
    //Given
    TeacherEto oldteacher = new TeacherEto();
    oldteacher.setFirstName("Kamil");
    oldteacher.setLastName("Korek");
    this.teacherService.createNew(oldteacher);

    String oldName = this.trepo.findById(1L).get().getFirstName();

    Map<String, Object> info = new HashMap<>();
    info.put("firstName", "Kuba");

    //When
    TeacherEto teachereto = teacherService.partialUpdate(1L, info);

    String newName = this.trepo.findById(1L).get().getFirstName();

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
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);
    //when
    teacherService.delete(1L, Optional.of(2L));

    List<SubjectEntity> result = this.surepo.findAllByTeacherEntityIdIsNull();
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
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    SubjectEntity su = tec.saveTestSubject(cy, te);
    StudentEntity st = tec.saveTestStudent(cy);
    tec.saveTestGrade(te, st, su);

    //when
    teacherService.delete(1L, Optional.of(2L));

    List<Grade> result = this.grepo.findAllByTeacherEntityIdIsNull();
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
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    SubjectEntity se = tec.saveTestSubject(cy, te);

    Long oldId = surepo.findById(1L).get().getTeacherEntity().getId();

    //when
    teacherService.delete(1L, Optional.of(2L));

    Long newId = surepo.findById(1L).get().getTeacherEntity().getId();

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
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    SubjectEntity sue = tec.saveTestSubject(cy, te);
    StudentEntity ste = tec.saveTestStudent(cy);
    tec.saveTestGrade(te, ste, sue);

    Long oldId = grepo.findById(1L).get().getTeacherEntity().getId();

    //when
    teacherService.delete(1L, Optional.of(2L));

    Long newId = grepo.findById(1L).get().getTeacherEntity().getId();
    //then
    Assertions.assertThat(newId).isNotEqualTo(oldId);
    Assertions.assertThat(newId).isEqualTo(2L);
  }

  @Test
  public void deleteTeacherWithNotExistingNewIdShouldThrowException() {
    //Given
    TeacherEntity te = tec.saveTestTeacher();
    ClassYear cy = tec.saveTestClassYear();
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
    ClassYear cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);

    Assertions.assertThatThrownBy(() -> {
              //when
      this.teacherService.delete(1L, Optional.empty());
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

    List<TeacherEntity> teachers = this.trepo.findAll();
    //then
    Assertions.assertThat(teachers).isEmpty();
  }

  }
